package blockchyp;

import java.util.ArrayList;
import java.util.Arrays;
import com.blockchyp.client.APICredentials;
import com.blockchyp.client.BlockChypClient;
import com.blockchyp.client.dto.ClearTerminalRequest;
import com.blockchyp.client.dto.TransactionDisplayItem;
import com.blockchyp.client.dto.TransactionDisplayRequest;
import com.blockchyp.client.dto.TransactionDisplayTransaction;

public class BlockChypTest {
	
	private static String APIKEY = "";
	private static String SIGNINGKEY = "";
	private static String BEARERTOKEN = "";
	private static String TERMINAL = "";
	
	public static void main(String[] args) throws Exception {
		BlockChypClient client = getClient();
		
		// Initially clear the terminal
		clearTerminal(client);
		Thread.sleep(1000);
		
		// Add initial items with id: 1, 2
		addLineItem(client);
		Thread.sleep(1000);
		
		// In the past, I believe this would overwrite the line items to be empty (new behavior?)
		clearLineItems(client);
		Thread.sleep(1000);
		
		// Add additional items with id: 1, 2 - This appears to now accumulate the qty based on id of the line items
		addLineItem(client);
		Thread.sleep(1000);
		
		// Clear the terminal - this gets rid of the old line items
		clearTerminal(client);
		Thread.sleep(1000);
		
		// Add a new set of line items with id: 1, 2 - This shows correctly
		addLineItem(client);
	}
	
	public static BlockChypClient getClient() {
		BlockChypClient blockChypClient = new BlockChypClient(new APICredentials(
				APIKEY, 
				BEARERTOKEN, 
				SIGNINGKEY
			)
		);
		return blockChypClient;
	}
	
	public static void clearTerminal(BlockChypClient blockChypClient) throws Exception {
        ClearTerminalRequest request = new ClearTerminalRequest();
        request.setTest(true);
        request.setTerminalName(TERMINAL);
        blockChypClient.clear(request);
	}
	
	public static void clearLineItems(BlockChypClient blockChypClient) throws Exception {
		TransactionDisplayTransaction tx = new TransactionDisplayTransaction();
        tx.setItems(new ArrayList<TransactionDisplayItem>());
        TransactionDisplayRequest request = new TransactionDisplayRequest();
        request.setTerminalName(TERMINAL);
        request.setTest(true);
        request.setTransaction(tx);
        request.setWaitForRemovedCard(true);
        request.setTimeout(10);
        blockChypClient.updateTransactionDisplay(request);
	}
	
	public static void addLineItem(BlockChypClient blockChypClient) throws Exception {
        
        TransactionDisplayTransaction tx = new TransactionDisplayTransaction();
        TransactionDisplayItem item = new TransactionDisplayItem();
        item.setId(String.valueOf(1));
        item.setDescription("Item Description");
        item.setPrice("$1.00");
        item.setQuantity(1);
        
        TransactionDisplayItem item2 = new TransactionDisplayItem();
        item2.setId(String.valueOf(2));
        item2.setDescription("Item Description");
        item2.setPrice("$1.00");
        item2.setQuantity(1);
        tx.setItems(Arrays.asList(item, item2));
        
        TransactionDisplayRequest request = new TransactionDisplayRequest();
        request.setTerminalName(TERMINAL);
        request.setTest(true);
        request.setTransaction(tx);
        request.setWaitForRemovedCard(true);
        request.setTimeout(10);
                
        blockChypClient.updateTransactionDisplay(request);
	}

}
