/*
 * Copyright 2013 Google Inc.
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

package com.google.worldcoin.params;

import com.google.worldcoin.core.NetworkParameters;
import com.google.worldcoin.core.Sha256Hash;
import com.google.worldcoin.core.Utils;

import static com.google.common.base.Preconditions.checkState;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends NetworkParameters {
    public MainNetParams() {
        super();
        interval = INTERVAL;
        intervalNew = INTERVAL_RE;
        targetTimespan = TARGET_TIMESPAN;
        targetTimespanNew = TARGET_TIMESPAN_RE;
        proofOfWorkLimit = Utils.decodeCompactBits(0x1e0fffffL);

        addressHeader = 73;
        dumpedPrivateKeyHeader = addressHeader + 128; //This is always addressHeader + 128
        //p2shHeader = 5; //We don't have this
        acceptableAddressCodes = new int[] { addressHeader };
        port = 11081;
        packetMagic = 0xFBC0B6DBL;
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
        /*
        checkpoints.put(1, new Sha256Hash("1a48c2bf97e0df6d4f03cd5cb0896ef43b04987048fbeb5ab2dc013335e40731"));
        checkpoints.put(39, new Sha256Hash("9d9a9c4c36d95f2188eb9c796deca87f8ac968e4d4ffe423d8445eea56109335"));
        checkpoints.put(74, new Sha256Hash("c5621f7aed66b5d34cd1dec8c9f01801ec2193acb0a80f6b3abdbcaeebe9c23f"));
        checkpoints.put(130, new Sha256Hash("7a5597740ef7b88e4cd664d8919752e1754d5fe93a1ee04f9844e3ca346475e6"));
        checkpoints.put(200, new Sha256Hash("9ebe1c0ee596d5a0552a10d6dc4ef40fad865ca3a178400ba6bcafaefa6320cb"));
        checkpoints.put(749499, new Sha256Hash("a30ea8d5be278f9d7097ffb6bb5fb9af4203f34289382adc4df11800c7e0292b"));
        checkpoints.put(749526, new Sha256Hash("27324520a2ecc22294018679426a12e9aed8b2ef09772b8523effccfae5523cc"));
        */
        //TODO Get actual Worldcoin checkpoints
        checkpoints.put(749526, new Sha256Hash("27324520a2ecc22294018679426a12e9aed8b2ef09772b8523effccfae5523cc"));
        checkpoints.put(1200001, new Sha256Hash("7ff57e3d5390326cfd83f09feb04afb8fef1d55fb8796ef346456e3ce1e586f4"));

        dnsSeeds = new String[] {
        		 "seednode1.worldcoincore.com",
        		 "seednode2.worldcoincore.com",
        		 "seednode3.worldcoincore.com",
        		 "seednode4.worldcoincore.com"
        		 //,        		 "seednode5.worldcoincore.com",        		 "seednode6.worldcoincore.com" 
        };
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }
}
