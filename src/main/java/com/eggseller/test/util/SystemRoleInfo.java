package com.skbp.admin.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SystemRoleInfo {

	SYSTEM_ADMIN("R001", "SYSTEM_ADMIN", "시스템관리자"),
	LEGAL_ADMIN("R007", "LEGAL_ADMIN", "법무관리자"),
	RECEIVE_USER("R008", "RECEIVER", "수령담당자");

	private String roleId;
	private String roleName;
	private String roleKrName;
}
