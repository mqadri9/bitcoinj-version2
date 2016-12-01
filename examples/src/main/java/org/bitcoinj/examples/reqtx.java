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
public class reqtx {
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
        //File part1=new File("/home/mqadri/Desktop/part1.txt");
        //File part2=new File("/home/mqadri/Desktop/part2.txt");
        //File[] parts={part1,part2};
        Path path1=Paths.get("/home/mqadri/Desktop/part1.txt");
        Path path2=Paths.get("/home/mqadri/Desktop/part2.txt");
        /*byte[] part1=Files.readAllBytes(path1);
        byte[] part2=Files.readAllBytes(path2);
        byte[][] parts={part1,part2};
        int i;
        Address address4 = wallet4.wallet().currentReceiveAddress();
        Address address3 = wallet3.wallet().currentReceiveAddress();
        Set<Transaction> transactions = wallet3.wallet().getTransactions(true);
        for (Transaction tx : transactions){
        	List<TransactionOutput> txOutput=tx.getOutputs();
        	for (TransactionOutput txx : txOutput){
        		if (txx.getScriptPubKey().isSentToChecksumContract()) {
        				for(i=0;i<parts.length;i++){ 
        			        SendRequest req = SendRequest.to(address4, COIN.divide(1000));
        			        wallet3.wallet().completeTxChecksum(req,parts[i]);
		
        				}
        		}
        	}
        }*/
        
        
        
        
        //temp = (Transaction) transactions.toArray()[0];
        //TransactionOutput output = temp.getOutput(0);
        Address address4 = wallet4.wallet().currentReceiveAddress();
        SendRequest req = SendRequest.to(address4, COIN.multiply(1));
        //req.tx.addInput(output);
        
        wallet3.wallet().completeTx(req);
        
        /*wallet3.wallet().signTransaction(req);
        req.tx.getConfidence().setSource(TransactionConfidence.Source.SELF);
        req.tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        req.tx.setExchangeRate(req.exchangeRate);
        req.tx.setMemo(req.memo);
        req.completed = true; */
        System.out.println(req.tx);
        final Peer peer = wallet3.peerGroup().getConnectedPeers().get(0);
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
