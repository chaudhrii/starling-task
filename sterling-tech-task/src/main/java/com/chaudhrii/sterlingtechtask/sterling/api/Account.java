package com.chaudhrii.sterlingtechtask.sterling.api;

import lombok.Data;

@Data
public class Account {
	private String accountUid;
	private String accountType;
	private String defaultCategory;
	private String currency;
	private String createdAt;
	private String name;
}
