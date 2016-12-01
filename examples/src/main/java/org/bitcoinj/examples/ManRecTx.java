package org.bitcoinj.examples;




import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.SendRequest;

import java.io.File;
import java.util.*;

import javax.annotation.Nullable;

import static org.bitcoinj.core.Coin.*;
public class ManRecTx {
    
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
	        byte[] checksum2 = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309e");
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
        
	        TransactionOutput output;
	        Transaction temp;
	        Address address4 = wallet4.wallet().currentReceiveAddress();
	        Set<Transaction> transactions = wallet3.wallet().getTransactions(true);

//	        System.out.println("isEmpty "+transactions.isEmpty()+"\n");
	        temp = new Transaction(params, hexStringToByteArray("") );
//	        temp = (Transaction) transactions.toArray()[0];
	        
	        output = temp.getOutput(0);	        
	        System.out.println("Tansaction output:\n"+output);	 
	        try{
			       
		        Transaction contract = new Transaction(params); 

		        // Sender: sig pubk [dup() hash() pubk equ() chksig() OP_VERIFY() chksm equchksm()]
		        // Receiver: chksm sig pubk [dup() hash() pubk equ() chksig()]
		        
		        Script locking = new ScriptBuilder()
						.op(OP_DUP)
						.op(OP_HASH160)
						.data(address4.getHash160())
						.op(OP_EQUALVERIFY)
						.op(OP_CHECKSIG)
						.build();
		        
		        ECKey signingKey = new ECKey();
		        //TODO maybe signTransaction in wallet can be edited so it will take checksum contracts
		        	//TODO instead of having to fix signTransaction we can add new TransactionSigner class.

		        Script unlocking = ScriptBuilder.createInputScript(null, signingKey, checksum1);		      
		        
		        contract.addOutput(COIN.multiply(12), locking);
			    
			    //TODO way to add the inputs. 
		        // the problem here is that it is very hard to get the uncompressed transaction id, temp.
		        // however, getting the uncomp txid is a wallet class problem.
		        //
		        // also, I'm not sure what getProgram() is doing, and using this function to initialize
		        // transaction input isn't right.. it gives arrayoutofbounds exception.
		        //
			    contract.addInput(new TransactionInput(params, temp, unlocking.getProgram(), 0));
			    
			    
			    
			    contract.addSignedInput(output, signingKey);
		        SendRequest req = SendRequest.forTx(contract);	        
			    wallet3.wallet().completeTx(req);
			    wallet3.peerGroup().broadcastTransaction(req.tx);
			    System.out.println(req.tx);

	       
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("Exception:\n"+e);
	        } 
	        
	        
	        
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


/* Trash
 
 
 Script unlocking = new ScriptBuilder()
		        		.data(checksum2)
		        		.data(signingKey.getSecretBytes())
		        		.data(address4.getHash160()) //problematic .. should be signature
		        		.build();




*/