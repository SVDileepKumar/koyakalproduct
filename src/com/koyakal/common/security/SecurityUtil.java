package com.koyakal.common.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class SecurityUtil {

	/**
	 * Returns the encrypted text for the given parameter. MD5 algorithm is used
	 * for the encryption
	 * 
	 * @param productid
	 *            - product id that will be served by the client
	 * @return encrypted product id
	 * @throws ArmourException 
	 */
	public static String encrypt(String productid, String key) throws ArmourException {
		String encryptedText = "";

		try {
			
			Key tempKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, tempKey);
			byte[] encVal = c.doFinal(productid.getBytes());
			encryptedText = Base64.encodeBase64URLSafeString(encVal);
			
		} catch (NoSuchAlgorithmException 
				| NoSuchPaddingException | InvalidKeyException
				
				| IllegalBlockSizeException NoEx) {
			throw new ArmourException(NoEx.getMessage());
		}catch(BadPaddingException NoEx1){
			throw new ArmourException(ArmourErrorCode.AMR_ERR_006);
		}

		return encryptedText;
	}

	/**
	 * Returns the decrypted text for the given text which was encrypted by MD5.
	 * The decryption is done by following the MD5 algorithm
	 * 
	 * @param encrpyedProductID
	 *            id that is a encrpted product id
	 * @return decrpyted product id to the client
	 * @throws ArmourException 
	 */

	public static String decrypt(String encrpyedProductID,String key) throws ArmourException {

		String decryptedText = "";
		try {
			Key tempKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, tempKey);
			byte[] decordedValue = Base64.decodeBase64(encrpyedProductID);
			byte[] decValue = c.doFinal(decordedValue);
			decryptedText = new String(decValue);
				
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException NoEx) {
			throw new ArmourException(NoEx.getMessage());
		}catch(BadPaddingException NoEx1){
			throw new ArmourException(ArmourErrorCode.AMR_ERR_006);
		}
		return decryptedText;
	}

}

