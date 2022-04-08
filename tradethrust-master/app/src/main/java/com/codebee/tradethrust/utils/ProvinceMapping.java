package com.codebee.tradethrust.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csangharsha on 6/18/18.
 */

public class ProvinceMapping {

    public static Map<String, ArrayList<String>> provinceMap = new LinkedHashMap<>();
    public static Map<String, ArrayList<String>> zoneMap = new LinkedHashMap<>();

    static {
        provinceMap.put("Province No. 1", new ArrayList<>(Arrays.asList(
                "Koshi",
                "Mechi",
                "Sagarmatha"
        )));

        provinceMap.put("Province No. 2", new ArrayList<>(Arrays.asList(
                "Janakpur",
                "Sagarmatha",
                "Narayani"
        )));

        provinceMap.put("Province No. 3", new ArrayList<>(Arrays.asList(
                "Bagmati",
                "Janakpur",
                "Narayani"
        )));

        provinceMap.put("Province No. 4", new ArrayList<>(Arrays.asList(
                "Dhawalagiri",
                "Gandaki",
                "Lumbini"
        )));

        provinceMap.put("Province No. 5", new ArrayList<>(Arrays.asList(
                "Bheri",
                "Lumbini",
                "Rapti"
        )));

        provinceMap.put("Province No. 6", new ArrayList<>(Arrays.asList(
                "Bheri",
                "Karnali",
                "Rapti"
        )));

        provinceMap.put("Province No. 7", new ArrayList<>(Arrays.asList(
                "Mahakali",
                "Seti"
        )));

        zoneMap.put("Koshi_1", new ArrayList<String>(Arrays.asList(
                "Bhojpur",
                "Dhankuta",
                "Morang",
                "Sankhuwasabha",
                "Sunsari",
                "Terhathum"
        )));

        zoneMap.put("Mechi_1", new ArrayList<String>(Arrays.asList(
                "Ilam",
                "Jhapa",
                "Panchthar",
                "Taplejung"
        )));

        zoneMap.put("Sagarmatha_1", new ArrayList<String>(Arrays.asList(
                "Khotang",
                "Okhaldhunga",
                "Solukhumbu",
                "Udayapur"
        )));

        zoneMap.put("Janakpur_2", new ArrayList<String>(Arrays.asList(
                "Dhanusa",
                "Mahottari",
                "Sarlahi"
        )));

        zoneMap.put("Sagarmatha_2", new ArrayList<String>(Arrays.asList(
                "Bara",
                "Parsa",
                "Rautahat"
        )));

        zoneMap.put("Narayani_2", new ArrayList<String>(Arrays.asList(
                "Saptari",
                "Siraha"
        )));

        zoneMap.put("Bagmati_3", new ArrayList<String>(Arrays.asList(
                "Bhaktapur",
                "Dhading",
                "Kathmandu",
                "Kavrepalanchok",
                "Lalitpur",
                "Nuwakot",
                "Rasuwa",
                "Sindhupalchok"
        )));

        zoneMap.put("Janakpur_3", new ArrayList<String>(Arrays.asList(
                "Dolakha",
                "Ramechhap",
                "Sindhuli"
        )));

        zoneMap.put("Narayani_3", new ArrayList<String>(Arrays.asList(
                "Chitawan",
                "Makwanpur"
        )));

        zoneMap.put("Dhawalagiri_4", new ArrayList<String>(Arrays.asList(
                "Baglung",
                "Mustang",
                "Myagdi",
                "Parbat"
        )));

        zoneMap.put("Gandaki_4", new ArrayList<String>(Arrays.asList(
                "Gorkha",
                "Kaski",
                "Lamjung",
                "Syangja",
                "Tanahu",
                "Manang"
        )));

        zoneMap.put("Lumbini_4", new ArrayList<String>(Arrays.asList(
                "Nawalparasi"
        )));

        zoneMap.put("Lumbini_5", new ArrayList<String>(Arrays.asList(
                "Arghakhanchi",
                "Gulmi",
                "Kapilvastu",
                "Palpa",
                "Rupandehi"
        )));

        zoneMap.put("Rapti_5", new ArrayList<String>(Arrays.asList(
                "Dang",
                "Pyuthan",
                "Rolpa",
                "Rukum"
        )));

        zoneMap.put("Bheri_5", new ArrayList<String>(Arrays.asList(
                "Banke",
                "Bardiya"
        )));

        zoneMap.put("Bheri_6", new ArrayList<String>(Arrays.asList(
                "Dailekh",
                "Jajarkot",
                "Surkhet"
        )));

        zoneMap.put("Karnali_6", new ArrayList<String>(Arrays.asList(
                "Dolpa",
                "Humla",
                "Jumla",
                "Kalikot",
                "Mugu"
        )));

        zoneMap.put("Rapti_6", new ArrayList<String>(Arrays.asList(
                "Salyan"
        )));

        zoneMap.put("Mahakali_7", new ArrayList<String>(Arrays.asList(
                "Baitadi",
                "Dadeldhura",
                "Darchula",
                "Kanchanpur"
        )));

        zoneMap.put("Seti_7", new ArrayList<String>(Arrays.asList(
                "Achham",
                "Bajhang",
                "Doti",
                "Kailali",
                "Bajura"
        )));

    }

    public static String getDistrictKey(String provinceName, String zoneName){
        return zoneName + "_" + provinceName.replace("Province No. ", "");
    }

}
