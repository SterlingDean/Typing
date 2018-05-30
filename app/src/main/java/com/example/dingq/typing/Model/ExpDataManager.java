package com.example.dingq.typing.Model;

import android.os.Environment;

import com.example.dingq.typing.Activity.MainActivity;

import java.io.File;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

// 用于管理实验数据
public class ExpDataManager {
    private MainActivity mActivity;
    private ArrayList<ArrayList<SingleTrialResult>> expData; // 实验数据：block-trial-SingleTrialResult

    public ExpDataManager(MainActivity activity) {
        mActivity = activity;
        expData = new ArrayList<>();
        for (int i = 0; i < 12; i++) { // 共12个block
            expData.add(new ArrayList<SingleTrialResult>());
        }
    }

    // 记录超时trial
    public void recordTimeoutTrial(int block) {
        expData.get(block).add(new SingleTrialResult());
    }

    // 记录正常trial
    public void recordSingleTrial(int block, boolean acc, int rt) {
        expData.get(block).add(new SingleTrialResult(acc, rt));
    }

    // 将实验数据输出为excel文件
    public void exportDataToExcel(String subId) {
        String fileName = getExcelDir() + File.separator + "subject" + subId + ".xls";
        createExcel(new File(fileName));
    }

    // 获取excel文件的存储目录
    private String getExcelDir() {
        // SD卡指定文件夹
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        File dir = new File(sdcardPath + File.separator + "Typing" + File.separator + "ExpData");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.toString();
    }

    // 创建excel文件并写入实验数据
    private void createExcel(File file) {
        WritableSheet writableSheet;
        try {
            if (!file.exists()) {
                ArrayList<ArrayList> stlTypeOrder = mActivity.getStlTyeOrder(); // 本次实验的刺激类型呈现顺序：block-trial-int
                ArrayList<String> blockOrder = mActivity.getCurrentBlockOrder(); // 本次实验block顺序
                int blockNum = expData.size();
                int trialNum = expData.get(0).size();

                // 创建sheet1
                WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
                writableSheet = writableWorkbook.createSheet("sheet1", 0);
                // 打表头
                int RTCol = 0;
                int ACCCol = 1;
                int StlTypeCol = 2;
                int KbTypeCol = 3;
                int AvgRTCol = 6;
                int AvgACCCol = 7;
                writableSheet.addCell(new Label(RTCol, 0, "RT"));
                writableSheet.addCell(new Label(ACCCol, 0, "ACC"));
                writableSheet.addCell(new Label(StlTypeCol, 0, "StlType"));
                writableSheet.addCell(new Label(KbTypeCol, 0, "KbType"));
                writableSheet.addCell(new Label(AvgRTCol, 0, "AvgRT"));
                writableSheet.addCell(new Label(AvgACCCol, 0, "AvgACC"));

                // 写数据
                for (int i = 0; i < blockNum; i++) {
                    for (int j = 0; j < trialNum; j++) { // trial
                        SingleTrialResult data = expData.get(i).get(j);

                        if (data.isTimeout()) { // 超时trial，不记录RT，ACC记为9
                            writableSheet.addCell(new Label(ACCCol, i * trialNum + j + 1, "9"));
                        } else { // 正常trial，记录RT、ACC
                            writableSheet.addCell(new Label(RTCol, i * trialNum + j + 1, "" + data.getRT()));
                            // ACC，正确记为1，错误记为0
                            if (data.getACC()) {
                                writableSheet.addCell(new Label(ACCCol, i * trialNum + j + 1, "1"));
                            } else {
                                writableSheet.addCell(new Label(ACCCol, i * trialNum + j + 1, "0"));
                            }
                        }
                        // 记录刺激、键盘类型
                        writableSheet.addCell(new Label(StlTypeCol, i * trialNum + j + 1, "" + stlTypeOrder.get(i).get(j)));
                        writableSheet.addCell(new Label(KbTypeCol, i * trialNum + j + 1, blockOrder.get(i)));
                    }
                    // 统计block的平均RT、ACC
                    writableSheet.addCell(new Label(AvgRTCol - 1, i + 1, "block_" + blockOrder.get(i)));
                    writableSheet.addCell(new Label(AvgRTCol, i + 1, "" + getBlockAvgRT(i)));
                    writableSheet.addCell(new Label(AvgACCCol, i + 1, "" + getBlockAvgACC(i)));
                }
                // 从内存中写入文件
                writableWorkbook.write();
                writableWorkbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获得一个block的平均RT
    private float getBlockAvgRT(int block) {
        ArrayList<SingleTrialResult> blockData = expData.get(block);
        int totalRT = 0;
        for (int i = 0; i < blockData.size(); i++) {
            totalRT += blockData.get(i).getRT();
        }
        return (float) totalRT / getBlockValidTrialNum(block);
    }

    // 获得一个block的平均ACC
    private float getBlockAvgACC(int block) {
        ArrayList<SingleTrialResult> blockData = expData.get(block);
        int totalACC = 0;
        for (int i = 0; i < blockData.size(); i++) {
            if (blockData.get(i).getACC()) {
                totalACC++;
            }
        }
        return (float) totalACC / getBlockValidTrialNum(block);
    }

    // 获得一个block中的有效trial数
    private int getBlockValidTrialNum(int block) {
        ArrayList<SingleTrialResult> blockData = expData.get(block);
        int validTrialNum = 0;
        for (int i = 0; i < blockData.size(); i++) {
            if (!blockData.get(i).isTimeout()) {
                validTrialNum++;
            }
        }
        return validTrialNum;
    }
}
