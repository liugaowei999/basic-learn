package train;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

import javax.json.Json;
import java.math.BigDecimal;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataProcess {

    private static void loadData(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = null;
        StringBuffer sb = new StringBuffer();

        if (file.isFile() && file.exists()) {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(file);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
//                System.out.write(tempbytes, 0, byteread);
                String str = new String(tempbytes, 0, byteread);
                sb.append(str);
            }
        } else {
            System.out.println("找不到指定的文件，请确认文件路径是否正确");
        }


    }

    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为 "+in.available()/1024/1024 + "M");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {

        Map<Integer, List<TrainData>> startCityMap = new HashMap<>();
        Map<Integer, List<TrainData>> endCityMap = new HashMap<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (line == 1) {
                    line++;
                    continue;
                }
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                line++;


                TrainData trainData = getTrainDataObject(tempString);
//                System.out.println(trainData.getEnd_city_id());

                if (startCityMap.containsKey(trainData.getStart_city_id())) {
                    startCityMap.get(trainData.getStart_city_id()).add(trainData);
                } else {
                    List<TrainData>  trainDataList = Lists.newArrayList();
                    trainDataList.add(trainData);
                    startCityMap.put(trainData.getStart_city_id(), trainDataList);
                }

                if (endCityMap.containsKey(trainData.getEnd_city_id())) {
                    endCityMap.get(trainData.getEnd_city_id()).add(trainData);
                } else {
                    List<TrainData>  trainDataList = Lists.newArrayList();
                    trainDataList.add(trainData);
                    endCityMap.put(trainData.getEnd_city_id(), trainDataList);
                }

            }
            reader.close();

            //
            getLine(startCityMap, endCityMap);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private static void getLine(Map<Integer, List<TrainData>> startCityMap, Map<Integer, List<TrainData>> endCityMap) {
        Integer startCityId = 4535;
        Integer endCityId = 8890;
        List<TrainData> startTrainData = startCityMap.get(startCityId);
        List<TrainData> endTrainData = endCityMap.get(endCityId);
        System.out.println(startTrainData.size());
        System.out.println(endTrainData.size());

        Set<Integer> m1Set = startTrainData.stream().map(TrainData::getEnd_city_id)
                .collect(Collectors.toSet());

        Set<Integer> m2Set = endTrainData.stream().map(TrainData::getStart_city_id)
                .collect(Collectors.toSet());

        System.out.println(m1Set.size());
        System.out.println(m2Set.size());

        m1Set.removeAll(m2Set);
        System.out.println(m1Set.size());



        System.out.println(m1Set);


    }


    private static void getLineResult(Map<Integer, List<TrainData>> startCityMap, Map<Integer, List<TrainData>> endCityMap) {
        Integer startCityId = 4535;
        Integer endCityId = 8890;
        List<TrainData> startTrainData = startCityMap.get(startCityId);
        for (TrainData t1 : startTrainData) {
            List<TrainData> trainDataList2 = startCityMap.get(t1.getEnd_city_id());
            for (TrainData t2 : trainDataList2) {
//                if (t1.timeIsOk(t2)) {
//                    //
//                }
            }
        }
    }

    private static TrainData getTrainDataObject(String tempString) {
        String[] trainArray = tempString.split(",");
        TrainData trainData = new TrainData();

        trainData.setTransport_code(Integer.valueOf(trainArray[0]));
        trainData.setStart_city_id(Integer.valueOf(trainArray[1]));
        trainData.setStart_station_id(Integer.valueOf(trainArray[2]));
        trainData.setStart_time(trainArray[3]);
        trainData.setStart_sequence(Integer.valueOf(trainArray[4]));
        trainData.setEnd_city_id(Integer.valueOf(trainArray[5]));
        trainData.setEnd_station_id(Integer.valueOf(trainArray[6]));
        trainData.setEnd_time(trainArray[7]);
        trainData.setEnd_sequence(Integer.valueOf(trainArray[8]));
        trainData.setRun_time(Integer.valueOf(trainArray[9]));
        trainData.setTraffic_type(trainArray[10]);
        trainData.setPrice(new BigDecimal(trainArray[11]));
        return trainData;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "D:\\1024\\中转联运基础数据\\火车线路直达班次表.csv";
        // loadData(fileName);

//        readFileByLines(fileName);
        double a = 123.4;
        int b = (int)a;
        System.out.println(18312%30);

        // 各种算法对应的hash code
        String input = "hello, world";
        // MD5
        System.out.println(Hashing.md5().hashBytes(input.getBytes()).asInt());
        // sha256
        System.out.println(Hashing.sha256().hashBytes(input.getBytes()).asInt());
        // sha512
        System.out.println(Hashing.sha512().hashBytes(input.getBytes()).asInt());
        // crc32
        System.out.println(Hashing.crc32().hashBytes(input.getBytes()).asInt());
        // MD5
        System.out.println(Hashing.md5().hashUnencodedChars(input).asInt());
    }
}
