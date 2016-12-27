package com;

import java.io.*;
import java.util.*;

public class RPCInfo {
  ArrayList<String> filePaths;
  HashMap<String, Integer> rpcVersion;
  HashMap<String, String> rpcAPP; //rpc belongs to which app.
  ArrayList<RPC> rpcList;

  public RPCInfo() {
    filePaths = new ArrayList<String>();
    rpcVersion = new HashMap<String, Integer>();
    rpcAPP = new HashMap<String, String>();
    rpcList = new ArrayList<RPC>();
  }

  public void setInfoFilePath(String str, int version) {
    filePaths.add(str);
    rpcVersion.put(str, version);
    if (str.contains("mr_rpc")) {
      rpcAPP.put(str, "MR");
    }
    else if (str.contains("hbase_rpc")) {
      rpcAPP.put(str, "HB");
    }
  }

  public void readFile() {
    try {
      /*ArrayList<String> path = new ArrayList<String>();
      path.add("resource/mr_rpc.txt");
      path.add("resource/mr_rpc_v1.txt");*/
      for (String i : filePaths) {
        InputStream rpcIn = RPCInfo.class.getClassLoader().getResourceAsStream(i);
        Reader rpcReader = new InputStreamReader(rpcIn);
        BufferedReader buf = new BufferedReader(rpcReader);
        String line;
        String[] words;
        while ((line = buf.readLine()) != null) {
          if (line.startsWith("//")) {
            continue;
          }
          words = line.split(" ");
          /*System.out.println("Debug >> : " + words[0]);
          System.out.println("Debug >> : " + words[1]);
          System.out.println("Debug >> : " + words[2]);*/
          int ind = 0;
          RPC rpcI = new RPC();
          rpcI.className = words[ind++];
          rpcI.ifaceName = words[ind++];
          rpcI.methodName = words[ind++];
          rpcI.version = rpcVersion.get(i);
          rpcI.app = rpcAPP.get(i);
          /*if (i.equals("resource/mr_rpc.txt")) {
            rpcI.version = 2;
          }
          else {
            rpcI.version = 1;
          }*/

          rpcI.paraNum = Integer.parseInt(words[ind++]);
          if (rpcI.paraNum == 0) { //useless rpc
            continue;
          }
          for (int j=0; j < rpcI.paraNum; j++) {
            rpcI.paraType.add(words[ind++]);
          }
          rpcList.add(rpcI);
        }
        buf.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean isRPCMethod(String cc, String method) {
    //is a RPC function? check class and method
    boolean rt = false;
    for (RPC rpcI : rpcList) {
      if (rpcI.className.equals(cc) &&
          rpcI.methodName.equals(method)) {
        //if (rpcI.version == 2) {
          rt = true;
        //}
      }
    }
    return rt;
  }

  public boolean isRPCCall(String iface, String method) {
    //is a RPC call? check iface and method
    boolean rt = false;
    for (RPC rpcI : rpcList) {
      if (rpcI.ifaceName.equals(iface) &&
          rpcI.methodName.equals(method)) {
        rt = true;
      }
    }
    return rt;
  }
  public boolean isTargetAPP(String iface, String method, String app) {
    //is target app? check iface and method
    boolean rt = false;
    for (RPC rpcI : rpcList) {
      if (rpcI.ifaceName.equals(iface) &&
          rpcI.methodName.equals(method)) {
        rt = rpcI.app.equals(app);
      }
    }
    return rt;
  }

  public int getNumPara(String iface, String method) {
    for (RPC rpcI : rpcList) {
      if (rpcI.ifaceName.equals(iface) &&
          rpcI.methodName.equals(method)) {
        return rpcI.paraNum;
      }
    }
    return 0;
  }

  public int getVersion(String iface, String method) {
    for (RPC rpcI : rpcList) {
      if ((rpcI.ifaceName.equals(iface) ||
           rpcI.className.equals(iface))  &&
          rpcI.methodName.equals(method)) {
        return rpcI.version;
      }
    }
    return 0;
  }

  public void debug() {
    System.out.println("RPCinfo debug start.");
    for (RPC rpcI : rpcList) {
      System.out.println("CC: " + rpcI.className + "i: " + rpcI.ifaceName + " m: " + rpcI.methodName);
    }
  }
}


class RPC {
  String className;
  String ifaceName;
  String methodName;
  int version; //mrv1 or mrv2
  String app; //MR, HB

  int paraNum;
  ArrayList<String> paraType = new ArrayList<String>();

}
