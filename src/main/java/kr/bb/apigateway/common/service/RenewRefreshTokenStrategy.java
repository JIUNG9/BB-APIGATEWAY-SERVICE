package kr.bb.apigateway.common.service;

import com.bit.lot.flower.auth.common.valueobject.BaseId;
import com.bit.lot.flower.auth.common.valueobject.Role;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


@Service
public interface RenewRefreshTokenStrategy<ID extends BaseId> {

  String renew(ID id, Role role, HttpServletRequest request,
      HttpServletResponse response);
}
