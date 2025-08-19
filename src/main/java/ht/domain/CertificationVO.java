package ht.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class CertificationVO implements Serializable {
	private static final long serialVersionUID = -4559717068852984459L;
	private String uuid;
	private String channel;
	private String expire_date;
	private String extra_data;
}
