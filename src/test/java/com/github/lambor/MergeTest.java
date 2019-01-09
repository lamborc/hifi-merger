////////////////////////////////////////////////////////////////////
//							_ooOoo_                               //
//						   o8888888o                              //
//						   88" . "88                              //
//						   (| ^_^ |)                              //
//						   O\  =  /O                              //
//						____/`---'\____                           //
//					  .'  \\|     |//  `.                         //
//					 /  \\|||  :  |||//  \                        //
//				    /  _||||| -:- |||||-  \                       //
//				    |   | \\\  -  /// |   |                       //
//					| \_|  ''\---/''  |   |                       //
//					\  .-\__  `-`  ___/-. /                       //
//				  ___`. .'  /--.--\  `. . ___                     //
//				."" '<  `.___\_<|>_/___.'  >'"".                  //
//			  | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//		      \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//		========`-.____`-.___\_____/___.-`____.-'========         //
//							 `=---='                              //
//		^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//		佛祖保佑             永无BUG   	        永不修改          //
// Copyright© 2018-2020 lambor, Inc.All rights reseved.           //
////////////////////////////////////////////////////////////////////
package com.github.lambor;

import java.io.File;
import java.util.List;

public class MergeTest {

    public static void main(String[] args){
        merge();
    }

    public static void merge(){
        String srcPath = "f:/imusic/";
        WaveAudioMerge waveAudioMerge = new WaveAudioMerge(srcPath);
        List<File> fileList = waveAudioMerge.findFiles();
        boolean b = waveAudioMerge.checkedFormat(fileList);
        if(b){
            waveAudioMerge.merge(fileList,waveAudioMerge.getOutFile());
        }
    }
}
