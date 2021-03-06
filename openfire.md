#搭建openfire

* 首先安装jdk,略过,因为很熟悉了.

* 然后到openfire官网下载安装包 [openfire_3_9_3.tar.gz](http://www.igniterealtime.org/downloads/index.jsp)

* tar -xvf ... 解压

* cd 到bin目录,执行 ./openfire start

* 访问127.0.0.1:9090 具体访问路径可以通过日志文件查看,在logs文件夹下

* 按照提示一路next初始化openfire,设置数据库,设置管理员密码

* 登录管理截面看看

#客户端

* spark是官方推荐的第一个客户端,但是在ubuntu上下载linux版本没有运行起来,
索性就不找客户端了,直接找一个java的xmpp类库试着开发一个客户端应该更有价值,
因为大多数使用openfire的人都是自己开发客户端的

* 经过多次搜索比较,最终选择了官方提供的`smack`类库,maven 依赖如下:

```
   <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-core</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
       <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-extensions</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
       <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-tcp</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
       <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-debug</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
       <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-java7</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
       <dependency>
           <groupId>org.igniterealtime.smack</groupId>
           <artifactId>smack-im</artifactId>
           <version>4.1.0-rc5</version>
       </dependency>
```

* smack api [官网文档](https://www.igniterealtime.org/builds/smack/docs/latest/documentation/) 或者 [github文档](https://github.com/igniterealtime/Smack/blob/master/documentation/index.md)

* 下午开写demo,和儿子玩一会去 :)

* 晚上做了一会demo,遇到了阻碍,因为用的是4.1的版本,jdk7,没有仔细看官方文档,导致demo跑不起来,一直报空指针
最后耗时好久才解决,办法是增加java7的依赖补丁.[该问题解决的网址](http://stackoverflow.com/questions/29046171/xmpp-client-using-smack-4-1-0-rc3-giving-nullpointerexception-during-login)

* 官网升级说明[网址](https://github.com/igniterealtime/Smack/wiki/Smack-4.1-Readme-and-Upgrade-Guide)

可运行的demo代码
```java
package com.fangger.smark;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by p0po on 15-4-4.
 */
public class Test {
    public static void conn(){
        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                //.setUsernameAndPassword("p0po", "1")
                .setServiceName("p0po-thinkpad-x240")
                .setHost("p0po-thinkpad-x240")
                .setPort(5222)
                .setCompressionEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                //.setDebuggerEnabled(true)
                .build()
                ;

        AbstractXMPPConnection conn2 = new XMPPTCPConnection(configuration);

        try {
            conn2.connect().login("p0po","1");

            Chat chat = ChatManager.getInstanceFor(conn2)
                    .createChat("admin@localhost", new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            System.out.println("Received message: " + message);
                        }
                    });
            chat.sendMessage("i am p0po");

            while (true){;}

            //conn2.disconnect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        conn();
    }
}

```

##注意事项

我使用的mysql数据库,需要新建一个数据库,例如openfire,用的是`utf-8编码`,
在openfire初始化数据库的时候会失败,
因为初始化数据库的脚本中的索引比较大,大于767字节,一个utf-8默认需要3个字节,最多支持的varchar是255,
所以需要修改数据库或者脚本.
因为我的是demo环境,直接需改了脚本,将所有255替换成了128,这样成功初始化.

##初始化MY_SQL脚本

```
# $Revision: 1650 $
# $Date: 2005-07-20 00:18:17 -0300 (Wed, 20 Jul 2005) $

CREATE TABLE ofUser (
  username              VARCHAR(64)     NOT NULL,
  plainPassword         VARCHAR(32),
  encryptedPassword     VARCHAR(128),
  name                  VARCHAR(100),
  email                 VARCHAR(100),
  creationDate          CHAR(15)        NOT NULL,
  modificationDate      CHAR(15)        NOT NULL,
  PRIMARY KEY (username),
  INDEX ofUser_cDate_idx (creationDate)
);

CREATE TABLE ofUserProp (
  username              VARCHAR(64)     NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  propValue             TEXT            NOT NULL,
  PRIMARY KEY (username, name)
);

CREATE TABLE ofUserFlag (
  username              VARCHAR(64)     NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  startTime             CHAR(15),
  endTime               CHAR(15),
  PRIMARY KEY (username, name),
  INDEX ofUserFlag_sTime_idx (startTime),
  INDEX ofUserFlag_eTime_idx (endTime)
);

CREATE TABLE ofPrivate (
  username              VARCHAR(64)     NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  namespace             VARCHAR(200)    NOT NULL,
  privateData           TEXT            NOT NULL,
  PRIMARY KEY (username, name, namespace(100))
);

CREATE TABLE ofOffline (
  username              VARCHAR(64)     NOT NULL,
  messageID             BIGINT          NOT NULL,
  creationDate          CHAR(15)        NOT NULL,
  messageSize           INTEGER         NOT NULL,
  stanza                TEXT            NOT NULL,
  PRIMARY KEY (username, messageID)
);

CREATE TABLE ofPresence (
  username              VARCHAR(64)     NOT NULL,
  offlinePresence       TEXT,
  offlineDate           CHAR(15)     NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE ofRoster (
  rosterID              BIGINT          NOT NULL,
  username              VARCHAR(64)     NOT NULL,
  jid                   VARCHAR(1024)   NOT NULL,
  sub                   TINYINT         NOT NULL,
  ask                   TINYINT         NOT NULL,
  recv                  TINYINT         NOT NULL,
  nick                  VARCHAR(128),
  PRIMARY KEY (rosterID),
  INDEX ofRoster_unameid_idx (username),
  INDEX ofRoster_jid_idx (jid(128))
);

CREATE TABLE ofRosterGroups (
  rosterID              BIGINT          NOT NULL,
  rank                  TINYINT         NOT NULL,
  groupName             VARCHAR(128)    NOT NULL,
  PRIMARY KEY (rosterID, rank),
  INDEX ofRosterGroup_rosterid_idx (rosterID)
);

CREATE TABLE ofVCard (
  username              VARCHAR(64)     NOT NULL,
  vcard                 MEDIUMTEXT      NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE ofGroup (
  groupName             VARCHAR(50)     NOT NULL,
  description           VARCHAR(128),
  PRIMARY KEY (groupName)
);

CREATE TABLE ofGroupProp (
  groupName             VARCHAR(50)     NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  propValue             TEXT            NOT NULL,
  PRIMARY KEY (groupName, name)
);

CREATE TABLE ofGroupUser (
  groupName             VARCHAR(50)     NOT NULL,
  username              VARCHAR(100)    NOT NULL,
  administrator         TINYINT         NOT NULL,
  PRIMARY KEY (groupName, username, administrator)
);

CREATE TABLE ofID (
  idType                INTEGER         NOT NULL,
  id                    BIGINT          NOT NULL,
  PRIMARY KEY (idType)
);

CREATE TABLE ofProperty (
  name        VARCHAR(100)              NOT NULL,
  propValue   TEXT                      NOT NULL,
  PRIMARY KEY (name)
);


CREATE TABLE ofVersion (
  name     VARCHAR(50)  NOT NULL,
  version  INTEGER  NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE ofExtComponentConf (
  subdomain             VARCHAR(128)    NOT NULL,
  wildcard              TINYINT         NOT NULL,
  secret                VARCHAR(128),
  permission            VARCHAR(10)     NOT NULL,
  PRIMARY KEY (subdomain)
);

CREATE TABLE ofRemoteServerConf (
  xmppDomain            VARCHAR(128)    NOT NULL,
  remotePort            INTEGER,
  permission            VARCHAR(10)     NOT NULL,
  PRIMARY KEY (xmppDomain)
);

CREATE TABLE ofPrivacyList (
  username              VARCHAR(64)     NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  isDefault             TINYINT         NOT NULL,
  list                  TEXT            NOT NULL,
  PRIMARY KEY (username, name),
  INDEX ofPrivacyList_default_idx (username, isDefault)
);

CREATE TABLE ofSASLAuthorized (
  username            VARCHAR(64)   NOT NULL,
  principal           TEXT          NOT NULL,
  PRIMARY KEY (username, principal(128))
);

CREATE TABLE ofSecurityAuditLog (
  msgID                 BIGINT          NOT NULL,
  username              VARCHAR(64)     NOT NULL,
  entryStamp            BIGINT          NOT NULL,
  summary               VARCHAR(128)    NOT NULL,
  node                  VARCHAR(128)    NOT NULL,
  details               TEXT,
  PRIMARY KEY (msgID),
  INDEX ofSecurityAuditLog_tstamp_idx (entryStamp),
  INDEX ofSecurityAuditLog_uname_idx (username)
);

# MUC Tables

CREATE TABLE ofMucService (
  serviceID           BIGINT        NOT NULL,
  subdomain           VARCHAR(128)  NOT NULL,
  description         VARCHAR(128),
  isHidden            TINYINT       NOT NULL,
  PRIMARY KEY (subdomain),
  INDEX ofMucService_serviceid_idx (serviceID)
);

CREATE TABLE ofMucServiceProp (
  serviceID           BIGINT        NOT NULL,
  name                VARCHAR(100)  NOT NULL,
  propValue           TEXT          NOT NULL,
  PRIMARY KEY (serviceID, name)
);

CREATE TABLE ofMucRoom (
  serviceID           BIGINT        NOT NULL,
  roomID              BIGINT        NOT NULL,
  creationDate        CHAR(15)      NOT NULL,
  modificationDate    CHAR(15)      NOT NULL,
  name                VARCHAR(50)   NOT NULL,
  naturalName         VARCHAR(128)  NOT NULL,
  description         VARCHAR(128),
  lockedDate          CHAR(15)      NOT NULL,
  emptyDate           CHAR(15)      NULL,
  canChangeSubject    TINYINT       NOT NULL,
  maxUsers            INTEGER       NOT NULL,
  publicRoom          TINYINT       NOT NULL,
  moderated           TINYINT       NOT NULL,
  membersOnly         TINYINT       NOT NULL,
  canInvite           TINYINT       NOT NULL,
  roomPassword        VARCHAR(50)   NULL,
  canDiscoverJID      TINYINT       NOT NULL,
  logEnabled          TINYINT       NOT NULL,
  subject             VARCHAR(100)  NULL,
  rolesToBroadcast    TINYINT       NOT NULL,
  useReservedNick     TINYINT       NOT NULL,
  canChangeNick       TINYINT       NOT NULL,
  canRegister         TINYINT       NOT NULL,
  PRIMARY KEY (serviceID,name),
  INDEX ofMucRoom_roomid_idx (roomID),
  INDEX ofMucRoom_serviceid_idx (serviceID)
);

CREATE TABLE ofMucRoomProp (
  roomID                BIGINT          NOT NULL,
  name                  VARCHAR(100)    NOT NULL,
  propValue             TEXT            NOT NULL,
  PRIMARY KEY (roomID, name)
);

CREATE TABLE ofMucAffiliation (
  roomID              BIGINT        NOT NULL,
  jid                 TEXT          NOT NULL,
  affiliation         TINYINT       NOT NULL,
  PRIMARY KEY (roomID,jid(70))
);

CREATE TABLE ofMucMember (
  roomID              BIGINT        NOT NULL,
  jid                 TEXT          NOT NULL,
  nickname            VARCHAR(128)  NULL,
  firstName           VARCHAR(100)  NULL,
  lastName            VARCHAR(100)  NULL,
  url                 VARCHAR(100)  NULL,
  email               VARCHAR(100)  NULL,
  faqentry            VARCHAR(100)  NULL,
  PRIMARY KEY (roomID,jid(70))
);

CREATE TABLE ofMucConversationLog (
  roomID              BIGINT        NOT NULL,
  sender              TEXT          NOT NULL,
  nickname            VARCHAR(128)  NULL,
  logTime             CHAR(15)      NOT NULL,
  subject             VARCHAR(128)  NULL,
  body                TEXT          NULL,
  INDEX ofMucConversationLog_time_idx (logTime)
);

# PubSub Tables

CREATE TABLE ofPubsubNode (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  leaf                TINYINT       NOT NULL,
  creationDate        CHAR(15)      NOT NULL,
  modificationDate    CHAR(15)      NOT NULL,
  parent              VARCHAR(100)  NULL,
  deliverPayloads     TINYINT       NOT NULL,
  maxPayloadSize      INTEGER       NULL,
  persistItems        TINYINT       NULL,
  maxItems            INTEGER       NULL,
  notifyConfigChanges TINYINT       NOT NULL,
  notifyDelete        TINYINT       NOT NULL,
  notifyRetract       TINYINT       NOT NULL,
  presenceBased       TINYINT       NOT NULL,
  sendItemSubscribe   TINYINT       NOT NULL,
  publisherModel      VARCHAR(15)   NOT NULL,
  subscriptionEnabled TINYINT       NOT NULL,
  configSubscription  TINYINT       NOT NULL,
  accessModel         VARCHAR(10)   NOT NULL,
  payloadType         VARCHAR(100)  NULL,
  bodyXSLT            VARCHAR(100)  NULL,
  dataformXSLT        VARCHAR(100)  NULL,
  creator             VARCHAR(128) NOT NULL,
  description         VARCHAR(128)  NULL,
  language            VARCHAR(128)  NULL,
  name                VARCHAR(50)   NULL,
  replyPolicy         VARCHAR(15)   NULL,
  associationPolicy   VARCHAR(15)   NULL,
  maxLeafNodes        INTEGER       NULL,
  PRIMARY KEY (serviceID, nodeID)
);

CREATE TABLE ofPubsubNodeJIDs (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  jid                 VARCHAR(128)  NOT NULL,
  associationType     VARCHAR(20)   NOT NULL,
  PRIMARY KEY (serviceID, nodeID, jid(70))
);

CREATE TABLE ofPubsubNodeGroups (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  rosterGroup         VARCHAR(100)   NOT NULL,
  INDEX ofPubsubNodeGroups_idx (serviceID, nodeID)
);

CREATE TABLE ofPubsubAffiliation (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  jid                 VARCHAR(128) NOT NULL,
  affiliation         VARCHAR(10)   NOT NULL,
  PRIMARY KEY (serviceID, nodeID, jid(70))
);

CREATE TABLE ofPubsubItem (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  id                  VARCHAR(100)  NOT NULL,
  jid                 VARCHAR(128)  NOT NULL,
  creationDate        CHAR(15)      NOT NULL,
  payload             MEDIUMTEXT    NULL,
  PRIMARY KEY (serviceID, nodeID, id)
);

CREATE TABLE ofPubsubSubscription (
  serviceID           VARCHAR(100)  NOT NULL,
  nodeID              VARCHAR(100)  NOT NULL,
  id                  VARCHAR(100)  NOT NULL,
  jid                 VARCHAR(128) NOT NULL,
  owner               VARCHAR(128) NOT NULL,
  state               VARCHAR(15)   NOT NULL,
  deliver             TINYINT       NOT NULL,
  digest              TINYINT       NOT NULL,
  digest_frequency    INT           NOT NULL,
  expire              CHAR(15)      NULL,
  includeBody         TINYINT       NOT NULL,
  showValues          VARCHAR(30)   NULL,
  subscriptionType    VARCHAR(10)   NOT NULL,
  subscriptionDepth   TINYINT       NOT NULL,
  keyword             VARCHAR(200)  NULL,
  PRIMARY KEY (serviceID, nodeID, id)
);

CREATE TABLE ofPubsubDefaultConf (
  serviceID           VARCHAR(100)  NOT NULL,
  leaf                TINYINT       NOT NULL,
  deliverPayloads     TINYINT       NOT NULL,
  maxPayloadSize      INTEGER       NOT NULL,
  persistItems        TINYINT       NOT NULL,
  maxItems            INTEGER       NOT NULL,
  notifyConfigChanges TINYINT       NOT NULL,
  notifyDelete        TINYINT       NOT NULL,
  notifyRetract       TINYINT       NOT NULL,
  presenceBased       TINYINT       NOT NULL,
  sendItemSubscribe   TINYINT       NOT NULL,
  publisherModel      VARCHAR(15)   NOT NULL,
  subscriptionEnabled TINYINT       NOT NULL,
  accessModel         VARCHAR(10)   NOT NULL,
  language            VARCHAR(128)  NULL,
  replyPolicy         VARCHAR(15)   NULL,
  associationPolicy   VARCHAR(15)   NOT NULL,
  maxLeafNodes        INTEGER       NOT NULL,
  PRIMARY KEY (serviceID, leaf)
);

# Finally, insert default table values.

INSERT INTO ofID (idType, id) VALUES (18, 1);
INSERT INTO ofID (idType, id) VALUES (19, 1);
INSERT INTO ofID (idType, id) VALUES (23, 1);
INSERT INTO ofID (idType, id) VALUES (26, 2);

INSERT INTO ofVersion (name, version) VALUES ('openfire', 21);

# Entry for admin user
INSERT INTO ofUser (username, plainPassword, name, email, creationDate, modificationDate)
    VALUES ('admin', 'admin', 'Administrator', 'admin@example.com', '0', '0');

# Entry for default conference service
INSERT INTO ofMucService (serviceID, subdomain, isHidden) VALUES (1, 'conference', 0);


```

