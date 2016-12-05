package org.bitcoinj.examples;
import static org.bitcoinj.core.Coin.COIN;

import java.io.File;
import java.util.Set;

import org.bitcoinj.core.*;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.SendRequest;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
public class reqtx2 {
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
        BriefLogFormatter.init();
        final RegTestParams params = RegTestParams.get();
        byte[] checksum1 = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        //byte[] checksum2 = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309e");
        boolean flag=true;
        int OP_EQUAL=0x87;  
        int OP_DUP=0x76;  
        int OP_HASH160=0xa9;  
        int OP_EQUALVERIFY=0x88;  
        int OP_CHECKSIG=0xac;  
        int OP_VERIFY=0x69;

        WalletAppKit wallet1 = new WalletAppKit(params, new File("."), "wallet1");
        WalletAppKit wallet2 = new WalletAppKit(params, new File("."), "wallet2");
        WalletAppKit wallet3 = new WalletAppKit(params, new File("."), "wallet3");
        WalletAppKit wallet4 = new WalletAppKit(params, new File("."), "wallet4");
	    
        
        wallet1.connectToLocalHost();
        wallet1.setAutoSave(false);
        wallet1.startAsync();
        wallet1.awaitRunning();
        wallet2.connectToLocalHost();
        wallet2.setAutoSave(false);
        wallet2.startAsync();
        wallet2.awaitRunning();
        wallet3.connectToLocalHost();
        wallet3.setAutoSave(false);
        wallet3.startAsync();
        wallet3.awaitRunning();
        wallet4.connectToLocalHost();
        wallet4.setAutoSave(false);
        wallet4.startAsync();
        wallet4.awaitRunning();
        Transaction temp;

        //Path path1=Paths.get("/home/mqadri/Desktop/part1.txt");
        //Path path2=Paths.get("/home/mqadri/Desktop/part2.txt");

        
       
        Address address4 = wallet4.wallet().currentReceiveAddress();
        SendRequest req = SendRequest.to(address4, COIN.multiply(1));
        req.tx.setLockTime(1480568338);
        wallet2.wallet().completeTx(req);
        

        System.out.println(req.tx);
        final Peer peer = wallet2.peerGroup().getConnectedPeers().get(0);
        peer.sendMessage(req.tx);
        Thread.sleep(1000);
        wallet1.stopAsync();
        wallet1.awaitTerminated();
        wallet2.stopAsync();
        wallet2.awaitTerminated();
        wallet3.stopAsync();
        wallet3.awaitTerminated();
        wallet4.stopAsync();
        wallet4.awaitTerminated();
   
	}	
}