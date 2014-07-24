package com.google.worldcoin.tools;

import com.google.worldcoin.core.*;
import com.google.worldcoin.net.discovery.DnsDiscovery;
import com.google.worldcoin.params.MainNetParams;
import com.google.worldcoin.utils.BriefLogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchMempool {
    private static Logger log = LoggerFactory.getLogger(WatchMempool.class);

    public static void main(String[] args) {
        BriefLogFormatter.init();
        NetworkParameters params = MainNetParams.get();
        PeerGroup peerGroup = new PeerGroup(params);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.addEventListener(new AbstractPeerEventListener() {
            @Override
            public void onTransaction(Peer peer, Transaction tx) {
                try {
                    if (tx.getOutputs().size() != 1) return;
                    if (!tx.getOutput(0).getScriptPubKey().isSentToRawPubKey()) return;
                    log.info("Saw raw pay to pubkey {}", tx);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        });
        peerGroup.start();
    }
}
