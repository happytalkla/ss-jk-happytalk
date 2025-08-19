package ht.domain;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class MemberDetails extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 931992126349182551L;
	private MemberVO memberVO;

	public MemberDetails(String memberUid, String password, Collection<? extends GrantedAuthority> authorities, MemberVO memberVO) {

		super(memberUid, password, authorities);
		this.memberVO = memberVO;
	}
}
