/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.falcon.api.taskq.impl;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

public class TaskLog {
	
	
    private long id;
    private String username;
    private String content;
    private String status;
    private Date producedTime;
    private Date takenTime;
    private Date consumedTime;
    private String producedBy;
    private String consumedBy;
    private long groupId;
    private String taskName;
    private String rspnMsg;


	public String getRspnMsg() {
		return rspnMsg;
	}

	public void setRspnMsg(String rspnMsg) {
		this.rspnMsg = rspnMsg;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getProducedTime() {
        return producedTime;
    }

    public void setProducedTime(Date producedTime) {
        this.producedTime = producedTime;
    }

    public Date getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(Date takenTime) {
        this.takenTime = takenTime;
    }

    public Date getConsumedTime() {
        return consumedTime;
    }

    public void setConsumedTime(Date consumedTime) {
        this.consumedTime = consumedTime;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public void setProducedBy(String producedBy) {
        this.producedBy = producedBy;
    }

    public String getConsumedBy() {
        return consumedBy;
    }

    public void setConsumedBy(String consumedBy) {
        this.consumedBy = consumedBy;
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {
        System.out.println("Your Host addr: " + InetAddress.getLocalHost().getHostAddress()); // often
                                                                                              // returns
                                                                                              // "127.0.0.1"
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();) {
            NetworkInterface e = n.nextElement();
            System.out.println(e.getDisplayName());

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();) {
                InetAddress addr = a.nextElement();
                if (addr instanceof Inet6Address) {
                    System.out.println("ipv6 addr:" + addr.getHostAddress());
                } else {
                    System.out.println("ipv4 addr:" + addr.getHostAddress());
                }
            }
        }
    }

    /**
     * Check if it's "local address" or "link local address" or
     * "loopbackaddress"
     * 
     * @param ip
     *            address
     * 
     * @return result
     */
    public static boolean isReservedAddr(InetAddress inetAddr) {
        if (inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress()) {
            return true;
        }

        return false;
    }
    
    private String dateTag;
    

    public String getDateTag() {
        return dateTag;
    }

    public void setDateTag(String dateTag) {
        this.dateTag = dateTag;
    }


}
