package com.google.worldcoin.examples;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.worldcoin.core.*;
import com.google.worldcoin.kits.WalletAppKit;
import com.google.worldcoin.net.discovery.IrcDiscovery;
import com.google.worldcoin.params.MainNetParams;
import com.google.worldcoin.utils.Threading;


public class TestWallet {

	private WalletAppKit appKit;

	public static void main(String[] args) throws Exception {
		new TestWallet().run();
	}

	public void run() throws Exception {
		NetworkParameters params = MainNetParams.get();
		
		appKit = new WalletAppKit(params, new File("."), "worldcoins") {
			@Override
			protected void onSetupCompleted() {
				if (wallet().getKeychainSize() < 1) {
					ECKey key = new ECKey();
					wallet().addKey(key);
				}
				
				peerGroup().setConnectTimeoutMillis(1000);
				
				System.out.println(appKit.wallet());
				
				peerGroup().addEventListener(new AbstractPeerEventListener() {
					@Override
					public void onPeerConnected(Peer peer, int peerCount) {
						super.onPeerConnected(peer, peerCount);
						System.out.println(String.format("onPeerConnected: %s %s",peer,peerCount));
					}
					@Override
					public void onPeerDisconnected(Peer peer, int peerCount) {
						super.onPeerDisconnected(peer, peerCount);
						System.out.println(String.format("onPeerDisconnected: %s %s",peer,peerCount));
					}
					@Override public void onBlocksDownloaded(Peer peer, Block block, int blocksLeft) {
						super.onBlocksDownloaded(peer, block, blocksLeft);
						System.out.println(String.format("%s blocks left (downloaded %s)",blocksLeft,block.getHashAsString()));
					}
					
					@Override public Message onPreMessageReceived(Peer peer, Message m) {
						System.out.println(String.format("%s -> %s",peer,m.getClass()));
						return super.onPreMessageReceived(peer, m);
					}
				},Threading.SAME_THREAD);
			}
		};
        IrcDiscovery irc = new IrcDiscovery("#worldcoin00");
        InetSocketAddress[] peers = irc.getPeers(5, TimeUnit.SECONDS);
        PeerAddress[] pa = new PeerAddress[peers.length];
        for (int i=0; i<peers.length;i++)
        {
            pa[i] = new PeerAddress(peers[i]);
        }
        appKit.setPeerNodes(pa);
		appKit.startAndWait();
	}

}
