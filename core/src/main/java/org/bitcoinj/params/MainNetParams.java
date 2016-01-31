/*
 * Copyright 2013 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import org.bitcoinj.core.*;
import org.bitcoinj.net.discovery.*;

import java.net.*;

import static com.google.common.base.Preconditions.*;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends AbstractBitcoinNetParams {
    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;

    public MainNetParams() {
        super();
        interval = INTERVAL;   // Worldcoin: Just switch around the variables _RE and ignore blocks up to 20160.
        intervalNew = INTERVAL_RE;
        targetTimespan = TARGET_TIMESPAN;
        targetTimespanNew = TARGET_TIMESPAN_RE;
        maxTarget = Utils.decodeCompactBits(0x1e0fffff); //1f0ffff0L);

        addressHeader = 73;
        dumpedPrivateKeyHeader = addressHeader + 128; //This is always addressHeader + 128
        
        p2shHeader = 135;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        port = 11081;
        packetMagic = 0xFBC0B6DBL;
        bip32HeaderPub = 0x0488B21E; //The 4 byte header that serializes in base58 to "xpub".
        bip32HeaderPriv =0x0488ADE4; //The 4 byte header that serializes in base58 to "xprv"

        genesisBlock.setDifficultyTarget(0x1e0ffff0L);
        genesisBlock.setTime(1368503907L);
        genesisBlock.setNonce(102158625L);

        id = ID_MAINNET;
        subsidyDecreaseBlockCount = 20160; // fix, can't simply divide by block # must use subsidyAtBlock();
        spendableCoinbaseDepth = 70;  // The depth of blocks required for a coinbase transaction to be spendable.
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("7231b064d3e620c55960abce2963ea19e1c3ffb6f5ff70e975114835a7024107"),
                genesisHash);  // genesis block

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
        // actual Worldcoin checkpoints at 2015-02-26
        checkpoints.put(749526, new Sha256Hash("27324520a2ecc22294018679426a12e9aed8b2ef09772b8523effccfae5523cc"));
        checkpoints.put(1200001, new Sha256Hash("7ff57e3d5390326cfd83f09feb04afb8fef1d55fb8796ef346456e3ce1e586f4"));
        checkpoints.put(1972653, new Sha256Hash("e692876261f0e9b0b32cb63a8d6ef284c6c32348cd91079e56a984a3396e8eda"));

        dnsSeeds = new String[] {
        		 "seednode1.worldcoincore.com",
        		 "seednode2.worldcoincore.com",
        		 "seednode3.worldcoincore.com",
        		 "seednode4.worldcoincore.com"
        		 //,        		 "seednode5.worldcoincore.com",        		 "seednode6.worldcoincore.com" 
        };

        httpSeeds = new HttpDiscovery.Details[] { };

        addrSeeds = new int[] {                    0xF80CF3A2 , 0x3D183B25 , 0x694B1BC6 , 0x84DFA5BC };
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
