<%@ page language="java"  pageEncoding="UTF-8"%>
<div class="sideMenu">
    <img src="${basePath}common/images/menu_top.gif" /><br />
    <div class="sideMenuContent">
    <ul class="menu">
    <li><a href="${basePath}index.do?act=logOut">Sign out</a></li>
    <li><a href="${basePath}index.do?act=modifyPassword">Account</a></li>
    <li><a href="${basePath}users.do?act=find">Profiles</a></li>
   
    <!-- insert_start id="menu" -->
    <li><a href="${basePath}book.do?act=find">Book</a></li>
    <!-- insert_end -->
    
    </ul>
    </div><!-- End of sideMenuContent -->
    <div><img src="${basePath}common/images/menu_bottom.gif" /></div>
</div><!-- End of sideMenu -->
