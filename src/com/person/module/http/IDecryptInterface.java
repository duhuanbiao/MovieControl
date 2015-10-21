package com.person.module.http;

import java.util.Map;

/**
 * 
 */
public abstract interface IDecryptInterface {
	public String decrypt(Map<String, String> header, byte[] data);
}
