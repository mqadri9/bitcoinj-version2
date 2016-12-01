package org.bitcoinj.examples;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.examples.customTx.*;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_DUP;
import static org.bitcoinj.script.ScriptOpCodes.OP_EQUAL;
import static org.bitcoinj.script.ScriptOpCodes.OP_EQUALVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_HASH160;
import static org.bitcoinj.script.ScriptOpCodes.OP_VERIFY;

import java.io.File;
import java.util.*;
import java.math.*;
public class sendCustom {
	
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
	public static void main(String[] args) throws Exception {
	      int OP_IF = 0x63;
	      int OP_CHECKLOCKTIMEVERIFY = 0xb1;
	      int OP_ELSE = 0x67;
	      int OP_ENDIF = 0x68;
	      int OP_DROP = 0x75;
	      int OP_VERIFYLOCKTIME=0xba;
		 	BriefLogFormatter.init();
	        final RegTestParams params = RegTestParams.get();
			int i;
	        //used md5
			String part1Checksum="31D5E3A6775EC192DD25468D91200A99";
			String part2Checksum="21F403AFECC3E2F977806C7718A4BE0EB006D971";
			WalletAppKit wallet2 = new WalletAppKit(params, new File("."), "wallet2");
			WalletAppKit wallet3 = new WalletAppKit(params, new File("."), "wallet3");
		    wallet2.connectToLocalHost();
		    wallet2.setAutoSave(false);
			wallet2.startAsync();
			wallet2.awaitRunning();
		    wallet3.connectToLocalHost();
		    wallet3.setAutoSave(false);
			wallet3.startAsync();
			wallet3.awaitRunning();
	        Address recWalletAddress = wallet3.wallet().currentReceiveAddress();    
	        Address sendWalletAddress = wallet2.wallet().currentReceiveAddress();    

			Coin amount =Coin.COIN.multiply(3);
			//customTx s=new customTx();
			String[] checkSumArray={""};
			checkSumArray[0]=part1Checksum;
			//checkSumArray[1]=part2Checksum;
			WalletAppKit sendKit=wallet2;
			int numTx=checkSumArray.length;
			Coin amountPerTx=amount.divide(2);
			for(i=0;i<numTx;i++){
				Transaction tx = new Transaction(params);    
				byte[] checksum=hexStringToByteArray(checkSumArray[i]);
				BigInteger time;
				time=BigInteger.valueOf(1480568338);
		        byte[] timeBytes = Utils.reverseBytes(Utils.encodeMPI(time, false));
				Script locking = new ScriptBuilder()

						.op(OP_IF)
							.op(OP_DUP)
							.op(OP_HASH160)
							.data(recWalletAddress.getHash160())
							.op(OP_EQUALVERIFY)
							.op(OP_CHECKSIG)
							.op(OP_VERIFY)
							.data(checksum)
							.op(OP_EQUAL)
						.op(OP_ELSE)
							.data(timeBytes)
							.op(OP_CHECKLOCKTIMEVERIFY)
							.op(OP_DROP)
							.op(OP_DUP)
							.op(OP_HASH160)
							.data(sendWalletAddress.getHash160())
							.op(OP_EQUALVERIFY)
							.op(OP_CHECKSIG)
						.op(OP_ENDIF) 
						.build(); 
						/*.op(OP_DUP)
						.op(OP_HASH160)
						.data(recWalletAddress.getHash160())
						.op(OP_EQUALVERIFY)
						.op(OP_CHECKSIG)
						.op(OP_VERIFY)
						.data(checksum)
						.op(OP_EQUAL)
						.build();*/
			tx.addOutput(amountPerTx, locking);
		//	tx.setLockTime(1480690000);
			SendRequest req = SendRequest.forTx(tx);
			sendKit.wallet().completeTx(req);
			sendKit.peerGroup().broadcastTransaction(req.tx);
	        final Peer peer = sendKit.peerGroup().getConnectedPeers().get(0);
	        peer.sendMessage(tx);
		    System.out.println("Transaction:\n"+req.tx);
			}
			Thread.sleep(5000);
			wallet3.stopAsync();
			wallet3.awaitTerminated();
			wallet2.stopAsync();
			wallet2.awaitTerminated();
	}
}
