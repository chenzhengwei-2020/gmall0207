package com.czw.gmall.service;

import com.czw.gmall.beans.UmsMember;
import com.czw.gmall.beans.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    public List<UmsMember> findAllUser();

    UmsMember login(UmsMember umsMember);

    void saveUmsMember(UmsMember umsMember);

    UmsMember checkOauthUser(UmsMember umsCheck);

    List<UmsMemberReceiveAddress> getUmsAddress(String memberId);

    UmsMemberReceiveAddress getReceiveAddressById(String deliveryAddressId);
}
