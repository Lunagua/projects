package com.gamedata.test;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class ShellTest {

	public static void main(String[] args) {
		//指明连接主机的IP地址,端口，用户和密码
				String hostname = "hadoop3";
				String username = "";
				String password = "";
				try {
					Connection conn = new Connection(hostname);
					System.out.println("ssh开始连接远程主机"+hostname);
					conn.connect();
					//ssh校验，认证
					boolean isAuthenticated =  conn.authenticateWithPassword(username, password);
					if (isAuthenticated == false){  
						throw new IOException("连接远程主机"+hostname+"失败,请检查！！！");
					}
					System.out.println("远程连接主机"+ hostname+"主机成功！！！");
					// 打开一个会话
					Session ssh = conn.openSession();
					String cmd = "shell_path" + " ll -h";
					ssh.execCommand(cmd);
//					MessageQueue.getGroupQueue().ozzie_flag=1;
					ssh.close();
					conn.close();
				}
				catch (IOException e){ 
					e.printStackTrace();
				}

	}

}
