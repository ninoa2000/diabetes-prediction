/*
 Navicat Premium Data Transfer

 Source Server         : 124.70.98.57
 Source Server Type    : MongoDB
 Source Server Version : 80000
 Source Host           : 124.70.98.57:8999
 Source Schema         : chronic_disease

 Target Server Type    : MongoDB
 Target Server Version : 80000
 File Encoding         : 65001

 Date: 30/05/2025 11:05:06
*/


// ----------------------------
// Collection structure for chat
// ----------------------------
db.getCollection("chat").drop();
db.createCollection("chat");

// ----------------------------
// Documents of chat
// ----------------------------
db.getCollection("chat").insert([ {
    _id: ObjectId("680dc20ab9255c3f25dac8a0")
} ]);

// ----------------------------
// Collection structure for chronic_disease_medications
// ----------------------------
db.getCollection("chronic_disease_medications").drop();
db.createCollection("chronic_disease_medications");

// ----------------------------
// Documents of chronic_disease_medications
// ----------------------------
db.getCollection("chronic_disease_medications").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d47"),
    name: "氨氯地平",
    disease: "高血压",
    usage: "每日一次，每次5-10mg，早餐后服用",
    sideEffects: "可能引起头痛、水肿、心悸等",
    precautions: "避免与葡萄柚汁同服，定期监测血压",
    imageUrl: "/images/medications/amlodipine.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.ChronicDiseaseMedication"
} ]);
db.getCollection("chronic_disease_medications").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d48"),
    name: "二甲双胍",
    disease: "糖尿病",
    usage: "每日2-3次，每次500-850mg，餐后服用",
    sideEffects: "可能引起胃肠道不适，如恶心、腹泻等",
    precautions: "肾功能不全者慎用，定期监测血糖",
    imageUrl: "/images/medications/metformin.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.ChronicDiseaseMedication"
} ]);
db.getCollection("chronic_disease_medications").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d49"),
    name: "阿司匹林",
    disease: "冠心病",
    usage: "每日一次，每次75-100mg，餐后服用",
    sideEffects: "可能引起胃肠道不适，增加出血风险",
    precautions: "有出血倾向者慎用，定期检查凝血功能",
    imageUrl: "/images/medications/aspirin.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.ChronicDiseaseMedication"
} ]);

// ----------------------------
// Collection structure for chronic_diseases
// ----------------------------
db.getCollection("chronic_diseases").drop();
db.createCollection("chronic_diseases");

// ----------------------------
// Documents of chronic_diseases
// ----------------------------
db.getCollection("chronic_diseases").insert([ {
    _id: ObjectId("6815c9c8d5c21719b7a22f77"),
    name: "糖尿病",
    description: "糖尿病是一种以高血糖为特征的代谢性疾病，长期存在的高血糖会导致各种组织，特别是眼、肾、心脏、血管、神经的慢性损害、功能障碍。",
    symptoms: "多饮、多食、多尿、体重减轻、疲劳、视力模糊、伤口愈合缓慢",
    causes: "遗传因素、环境因素、生活方式、肥胖、缺乏运动、高血压、高血脂等",
    prevention: "健康饮食、规律运动、控制体重、定期体检、戒烟限酒、保持良好心态",
    treatment: "药物治疗、饮食控制、运动疗法、血糖监测、胰岛素治疗、并发症防治",
    riskFactors: [
        "肥胖",
        "家族史",
        "缺乏运动",
        "高血压",
        "高血脂",
        "年龄大于45岁",
        "妊娠糖尿病史"
    ],
    imageUrl: "/images/diabetes.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.ChronicDisease"
} ]);
db.getCollection("chronic_diseases").insert([ {
    _id: ObjectId("6815c9c8d5c21719b7a22f78"),
    name: "高血压",
    description: "高血压是一种以动脉血压持续升高为特征的疾病，是心脑血管疾病的主要危险因素。",
    symptoms: "头痛、头晕、心悸、耳鸣、失眠、注意力不集中、记忆力减退",
    causes: "遗传因素、高盐饮食、肥胖、压力、缺乏运动、吸烟、饮酒、年龄增长",
    prevention: "低盐饮食、控制体重、戒烟限酒、规律运动、保持良好心态、定期监测血压",
    treatment: "药物治疗、生活方式调整、血压监测、并发症防治",
    riskFactors: [
        "高龄",
        "家族史",
        "高盐饮食",
        "肥胖",
        "缺乏运动",
        "吸烟",
        "饮酒",
        "压力大"
    ],
    imageUrl: "/images/hypertension.jpg",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.ChronicDisease"
} ]);
db.getCollection("chronic_diseases").insert([ {
    _id: ObjectId("6815c9c8d5c21719b7a22f79"),
    name: "心脏病",
    description: "心脏病是一类影响心脏结构和功能的疾病，包括冠心病、心肌病、心律失常等。",
    symptoms: "胸痛、胸闷、心悸、气短、疲劳、头晕、晕厥、下肢水肿",
    causes: "高血压、高血脂、糖尿病、吸烟、肥胖、缺乏运动、压力、遗传因素",
    prevention: "控制血压、血脂、血糖、戒烟、健康饮食、规律运动、控制体重、保持良好心态",
    treatment: "药物治疗、手术治疗、生活方式调整、康复训练、定期随访",
    riskFactors: [
        "高血压",
        "高血脂",
        "糖尿病",
        "吸烟",
        "肥胖",
        "缺乏运动",
        "家族史",
        "年龄增长"
    ],
    imageUrl: "/images/heart-disease.jpg",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.ChronicDisease"
} ]);

// ----------------------------
// Collection structure for doctors
// ----------------------------
db.getCollection("doctors").drop();
db.createCollection("doctors");

// ----------------------------
// Documents of doctors
// ----------------------------
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b5e06f6ed650d6b357d"),
    userId: "68121b5e06f6ed650d6b357c",
    name: "医生1",
    gender: "男",
    department: "外科",
    title: "主治医师",
    hospitalName: "北京大学第一医院",
    introduction: "从事外科临床工作6年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [
        "68121be8d4633f7a90142dcb",
        "68121b6606f6ed650d6b35c4",
        "681825e7166354369c8fa96b"
    ],
    yearsOfExperience: NumberInt("6"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:18.693Z"),
    updatedAt: ISODate("2025-05-18T02:06:44.65Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b357f"),
    userId: "68121b5f06f6ed650d6b357e",
    name: "医生2",
    department: "儿科",
    title: "副主任医师",
    hospitalName: "北京大学人民医院",
    introduction: "从事儿科临床工作7年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [
        "682a0c95ddb17b5eb2f375f1"
    ],
    yearsOfExperience: NumberInt("7"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:19.306Z"),
    updatedAt: ISODate("2025-05-18T16:36:57.092Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3581"),
    userId: "68121b5f06f6ed650d6b3580",
    name: "医生3",
    department: "妇产科",
    title: "主任医师",
    hospitalName: "北京大学第三医院",
    introduction: "从事妇产科临床工作8年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("8"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:19.526Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.526Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3583"),
    userId: "68121b5f06f6ed650d6b3582",
    name: "医生4",
    department: "眼科",
    title: "住院医师",
    hospitalName: "北京友谊医院",
    introduction: "从事眼科临床工作9年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("9"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:19.766Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.766Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3585"),
    userId: "68121b5f06f6ed650d6b3584",
    name: "医生5",
    department: "耳鼻喉科",
    title: "主治医师",
    hospitalName: "北京朝阳医院",
    introduction: "从事耳鼻喉科临床工作10年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("10"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:19.946Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.946Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b3587"),
    userId: "68121b6006f6ed650d6b3586",
    name: "医生6",
    department: "口腔科",
    title: "副主任医师",
    hospitalName: "北京天坛医院",
    introduction: "从事口腔科临床工作11年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("11"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:20.144Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.144Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b3589"),
    userId: "68121b6006f6ed650d6b3588",
    name: "医生7",
    department: "皮肤科",
    title: "主任医师",
    hospitalName: "北京安贞医院",
    introduction: "从事皮肤科临床工作12年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("12"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:20.336Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.336Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358b"),
    userId: "68121b6006f6ed650d6b358a",
    name: "医生8",
    department: "中医科",
    title: "住院医师",
    hospitalName: "北京儿童医院",
    introduction: "从事中医科临床工作13年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("13"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:20.526Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.526Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358d"),
    userId: "68121b6006f6ed650d6b358c",
    name: "医生9",
    department: "康复科",
    title: "主治医师",
    hospitalName: "北京妇产医院",
    introduction: "从事康复科临床工作14年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [
        "681825e7166354369c8fa96b"
    ],
    yearsOfExperience: NumberInt("14"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:20.725Z"),
    updatedAt: ISODate("2025-05-09T11:30:59.547Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358f"),
    userId: "68121b6006f6ed650d6b358e",
    name: "医生10",
    department: "急诊科",
    title: "主治医师",
    hospitalName: "北京中医医院",
    introduction: "从事急诊科临床工作15年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("15"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:20.905Z"),
    updatedAt: ISODate("2025-05-19T14:12:10.34Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3591"),
    userId: "68121b6106f6ed650d6b3590",
    name: "医生11",
    department: "麻醉科",
    title: "主任医师",
    hospitalName: "北京肿瘤医院",
    introduction: "从事麻醉科临床工作16年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("16"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:21.096Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.096Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3593"),
    userId: "68121b6106f6ed650d6b3592",
    name: "医生12",
    department: "放射科",
    title: "住院医师",
    hospitalName: "北京协和医院",
    introduction: "从事放射科临床工作17年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("17"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:21.265Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.265Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3595"),
    userId: "68121b6106f6ed650d6b3594",
    name: "医生13",
    department: "内科",
    title: "主治医师",
    hospitalName: "北京大学第一医院",
    introduction: "从事内科临床工作18年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("18"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:21.437Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.437Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3597"),
    userId: "68121b6106f6ed650d6b3596",
    name: "医生14",
    department: "外科",
    title: "副主任医师",
    hospitalName: "北京大学人民医院",
    introduction: "从事外科临床工作19年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("19"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:21.647Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.647Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3599"),
    userId: "68121b6106f6ed650d6b3598",
    name: "医生15",
    department: "儿科",
    title: "主任医师",
    hospitalName: "北京大学第三医院",
    introduction: "从事儿科临床工作20年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("20"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:21.856Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.856Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359b"),
    userId: "68121b6206f6ed650d6b359a",
    name: "医生16",
    department: "妇产科",
    title: "住院医师",
    hospitalName: "北京友谊医院",
    introduction: "从事妇产科临床工作21年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("21"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:22.086Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.086Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359d"),
    userId: "68121b6206f6ed650d6b359c",
    name: "医生17",
    department: "眼科",
    title: "主治医师",
    hospitalName: "北京朝阳医院",
    introduction: "从事眼科临床工作22年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("22"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:22.286Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.286Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359f"),
    userId: "68121b6206f6ed650d6b359e",
    name: "医生18",
    department: "耳鼻喉科",
    title: "副主任医师",
    hospitalName: "北京天坛医院",
    introduction: "从事耳鼻喉科临床工作23年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("23"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:22.455Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.455Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b35a1"),
    userId: "68121b6206f6ed650d6b35a0",
    name: "医生19",
    department: "口腔科",
    title: "主任医师",
    hospitalName: "北京安贞医院",
    introduction: "从事口腔科临床工作24年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("24"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:22.657Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.657Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b35a3"),
    userId: "68121b6206f6ed650d6b35a2",
    name: "医生20",
    department: "皮肤科",
    title: "住院医师",
    hospitalName: "北京儿童医院",
    introduction: "从事皮肤科临床工作5年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("5"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:22.848Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.848Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a5"),
    userId: "68121b6306f6ed650d6b35a4",
    name: "医生21",
    department: "中医科",
    title: "主治医师",
    hospitalName: "北京妇产医院",
    introduction: "从事中医科临床工作6年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("6"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:23.148Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.148Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a7"),
    userId: "68121b6306f6ed650d6b35a6",
    name: "医生22",
    department: "康复科",
    title: "副主任医师",
    hospitalName: "北京中医医院",
    introduction: "从事康复科临床工作7年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("7"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:23.343Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.343Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a9"),
    userId: "68121b6306f6ed650d6b35a8",
    name: "医生23",
    department: "急诊科",
    title: "主任医师",
    hospitalName: "北京肿瘤医院",
    introduction: "从事急诊科临床工作8年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("8"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:23.536Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.536Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35ab"),
    userId: "68121b6306f6ed650d6b35aa",
    name: "医生24",
    department: "麻醉科",
    title: "住院医师",
    hospitalName: "北京协和医院",
    introduction: "从事麻醉科临床工作9年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("9"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:23.696Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.696Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35ad"),
    userId: "68121b6306f6ed650d6b35ac",
    name: "医生25",
    department: "放射科",
    title: "主治医师",
    hospitalName: "北京大学第一医院",
    introduction: "从事放射科临床工作10年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("10"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:23.865Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.865Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35af"),
    userId: "68121b6406f6ed650d6b35ae",
    name: "医生26",
    department: "内科",
    title: "副主任医师",
    hospitalName: "北京大学人民医院",
    introduction: "从事内科临床工作11年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("11"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:24.036Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.036Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b1"),
    userId: "68121b6406f6ed650d6b35b0",
    name: "医生27",
    department: "外科",
    title: "主任医师",
    hospitalName: "北京大学第三医院",
    introduction: "从事外科临床工作12年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("12"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:24.215Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.215Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b3"),
    userId: "68121b6406f6ed650d6b35b2",
    name: "医生28",
    department: "儿科",
    title: "住院医师",
    hospitalName: "北京友谊医院",
    introduction: "从事儿科临床工作13年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("13"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:24.446Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.446Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b5"),
    userId: "68121b6406f6ed650d6b35b4",
    name: "医生29",
    department: "妇产科",
    title: "主治医师",
    hospitalName: "北京朝阳医院",
    introduction: "从事妇产科临床工作14年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("14"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:24.646Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.646Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b7"),
    userId: "68121b6406f6ed650d6b35b6",
    name: "医生30",
    department: "眼科",
    title: "副主任医师",
    hospitalName: "北京天坛医院",
    introduction: "从事眼科临床工作15年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("15"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:24.826Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.826Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35b9"),
    userId: "68121b6506f6ed650d6b35b8",
    name: "医生31",
    department: "耳鼻喉科",
    title: "主任医师",
    hospitalName: "北京安贞医院",
    introduction: "从事耳鼻喉科临床工作16年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("16"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:25.05Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.05Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35bb"),
    userId: "68121b6506f6ed650d6b35ba",
    name: "医生32",
    department: "口腔科",
    title: "住院医师",
    hospitalName: "北京儿童医院",
    introduction: "从事口腔科临床工作17年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("17"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:25.236Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.236Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35bd"),
    userId: "68121b6506f6ed650d6b35bc",
    name: "医生33",
    department: "皮肤科",
    title: "主治医师",
    hospitalName: "北京妇产医院",
    introduction: "从事皮肤科临床工作18年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("18"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:25.405Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.405Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35bf"),
    userId: "68121b6506f6ed650d6b35be",
    name: "医生34",
    department: "中医科",
    title: "副主任医师",
    hospitalName: "北京中医医院",
    introduction: "从事中医科临床工作19年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("19"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:25.615Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.615Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35c1"),
    userId: "68121b6506f6ed650d6b35c0",
    name: "医生35",
    department: "康复科",
    title: "主任医师",
    hospitalName: "北京肿瘤医院",
    introduction: "从事康复科临床工作20年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("20"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:25.817Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.817Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);
db.getCollection("doctors").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c3"),
    userId: "68121b6506f6ed650d6b35c2",
    name: "医生36",
    department: "急诊科",
    title: "住院医师",
    hospitalName: "北京协和医院",
    introduction: "从事急诊科临床工作21年，具有丰富的临床经验。",
    education: "医学博士",
    certificates: [
        "执业医师资格证",
        "医师执业证"
    ],
    patientIds: [ ],
    yearsOfExperience: NumberInt("21"),
    available: true,
    createdAt: ISODate("2025-04-30T12:45:26.017Z"),
    updatedAt: ISODate("2025-04-30T12:45:26.017Z"),
    _class: "com.example.chronicdisease.model.Doctor"
} ]);

// ----------------------------
// Collection structure for health_habits
// ----------------------------
db.getCollection("health_habits").drop();
db.createCollection("health_habits");

// ----------------------------
// Documents of health_habits
// ----------------------------
db.getCollection("health_habits").insert([ {
    _id: ObjectId("6815cecfc31651433d5f6d3f"),
    title: "每日步行",
    content: "每天坚持步行30分钟，可以改善心肺功能，降低血压和血糖水平。建议选择空气清新的公园或步道。",
    category: "运动",
    imageUrl: "/images/health-habits/walking.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.HealthHabit"
} ]);
db.getCollection("health_habits").insert([ {
    _id: ObjectId("6815cecfc31651433d5f6d40"),
    title: "太极养生",
    content: "太极拳动作缓慢柔和，适合中老年人练习，可以改善平衡能力，缓解关节疼痛。",
    category: "运动",
    imageUrl: "/images/health-habits/taichi.jpg",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.HealthHabit"
} ]);
db.getCollection("health_habits").insert([ {
    _id: ObjectId("6815cecfc31651433d5f6d41"),
    title: "规律作息",
    content: "保持固定的睡眠时间，建议晚上10点前入睡，保证7-8小时的睡眠时间。睡前避免使用电子设备。",
    category: "睡眠",
    imageUrl: "/images/health-habits/sleep.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.HealthHabit"
} ]);
db.getCollection("health_habits").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d42"),
    title: "正念冥想",
    content: "每天进行10-15分钟的正念冥想，可以帮助缓解压力，改善情绪，提高生活质量。",
    category: "心理",
    imageUrl: "/images/health-habits/meditation.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.HealthHabit"
} ]);

// ----------------------------
// Collection structure for latest_research
// ----------------------------
db.getCollection("latest_research").drop();
db.createCollection("latest_research");

// ----------------------------
// Documents of latest_research
// ----------------------------
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c8d5c21719b7a22f7a"),
    title: "新型糖尿病治疗药物研究进展",
    content: "最新研究表明，新型GLP-1受体激动剂在控制血糖和减轻体重方面效果显著，可降低心血管事件风险。",
    source: "中华糖尿病杂志",
    author: "张教授",
    publishDate: ISODate("2025-05-03T07:46:16.857Z"),
    imageUrl: "/images/research1.jpg",
    link: "https://example.com/research1",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c8d5c21719b7a22f7b"),
    title: "高血压患者生活方式干预研究",
    content: "一项为期5年的研究表明，综合生活方式干预可显著降低高血压患者的血压水平，减少心血管事件发生率。",
    source: "中国高血压杂志",
    author: "李教授",
    publishDate: ISODate("2025-05-03T07:46:16.955Z"),
    imageUrl: "/images/research2.jpg",
    link: "https://example.com/research2",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f7c"),
    title: "心脏病早期诊断新方法",
    content: "研究人员开发出一种新型生物标志物检测方法，可提前预测心脏病风险，准确率达90%以上。",
    source: "中国心血管病杂志",
    author: "王教授",
    publishDate: ISODate("2025-05-03T07:46:17.019Z"),
    imageUrl: "/images/research3.jpg",
    link: "https://example.com/research3",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f7d"),
    title: "糖尿病并发症防治新进展",
    content: "最新研究发现，严格控制血糖可显著降低糖尿病并发症风险，特别是视网膜病变和肾病。",
    source: "中华糖尿病杂志",
    author: "赵教授",
    publishDate: ISODate("2025-05-03T07:46:17.087Z"),
    imageUrl: "/images/research4.jpg",
    link: "https://example.com/research4",
    order: NumberInt("4"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f7e"),
    title: "高血压患者运动处方研究",
    content: "研究表明，规律的有氧运动可显著降低高血压患者的血压水平，改善血管功能。",
    source: "中国高血压杂志",
    author: "刘教授",
    publishDate: ISODate("2025-05-03T07:46:17.177Z"),
    imageUrl: "/images/research5.jpg",
    link: "https://example.com/research5",
    order: NumberInt("5"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f7f"),
    title: "心脏病康复治疗新方法",
    content: "新型心脏康复方案可显著提高心脏病患者的生活质量和运动能力，降低再入院率。",
    source: "中国心血管病杂志",
    author: "陈教授",
    publishDate: ISODate("2025-05-03T07:46:17.242Z"),
    imageUrl: "/images/research6.jpg",
    link: "https://example.com/research6",
    order: NumberInt("6"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f80"),
    title: "糖尿病营养治疗新进展",
    content: "个性化营养治疗方案可显著改善糖尿病患者的血糖控制和生活质量。",
    source: "中华糖尿病杂志",
    author: "孙教授",
    publishDate: ISODate("2025-05-03T07:46:17.313Z"),
    imageUrl: "/images/research7.jpg",
    link: "https://example.com/research7",
    order: NumberInt("7"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f81"),
    title: "高血压患者心理干预研究",
    content: "心理干预可显著改善高血压患者的心理状态和血压控制效果。",
    source: "中国高血压杂志",
    author: "周教授",
    publishDate: ISODate("2025-05-03T07:46:17.382Z"),
    imageUrl: "/images/research8.jpg",
    link: "https://example.com/research8",
    order: NumberInt("8"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f82"),
    title: "心脏病患者远程监护研究",
    content: "远程监护系统可显著提高心脏病患者的治疗依从性和生活质量。",
    source: "中国心血管病杂志",
    author: "吴教授",
    publishDate: ISODate("2025-05-03T07:46:17.448Z"),
    imageUrl: "/images/research9.jpg",
    link: "https://example.com/research9",
    order: NumberInt("9"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f83"),
    title: "糖尿病并发症早期筛查新方法",
    content: "新型筛查方法可早期发现糖尿病并发症，提高治疗效果。",
    source: "中华糖尿病杂志",
    author: "郑教授",
    publishDate: ISODate("2025-05-03T07:46:17.542Z"),
    imageUrl: "/images/research10.jpg",
    link: "https://example.com/research10",
    order: NumberInt("10"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f84"),
    title: "高血压患者药物治疗新进展",
    content: "新型降压药物可显著提高血压控制率，减少不良反应。",
    source: "中国高血压杂志",
    author: "马教授",
    publishDate: ISODate("2025-05-03T07:46:17.636Z"),
    imageUrl: "/images/research11.jpg",
    link: "https://example.com/research11",
    order: NumberInt("11"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);
db.getCollection("latest_research").insert([ {
    _id: ObjectId("6815c9c9d5c21719b7a22f85"),
    title: "心脏病患者生活方式干预研究",
    content: "综合生活方式干预可显著改善心脏病患者的生活质量和预后。",
    source: "中国心血管病杂志",
    author: "黄教授",
    publishDate: ISODate("2025-05-03T07:46:17.712Z"),
    imageUrl: "/images/research12.jpg",
    link: "https://example.com/research12",
    order: NumberInt("12"),
    _class: "com.example.chronicdisease.model.LatestResearch"
} ]);

// ----------------------------
// Collection structure for messages
// ----------------------------
db.getCollection("messages").drop();
db.createCollection("messages");

// ----------------------------
// Documents of messages
// ----------------------------
db.getCollection("messages").insert([ {
    _id: ObjectId("68121bf8d4633f7a90142dcc"),
    fromUserId: "68121be8d4633f7a90142dcb",
    fromUserName: "陈迅",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "我现在觉得身体还不错",
    isRead: true,
    createdAt: ISODate("2025-04-30T12:47:52.015Z"),
    updatedAt: ISODate("2025-04-30T12:48:17.053Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "那挺好的，我看你身体没问题",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("6814eb6803fe7c0a32a1f221"),
    fromUserId: "68121be8d4633f7a90142dcb",
    fromUserName: "陈迅",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "现在心情不是很好，很焦虑",
    isRead: true,
    createdAt: ISODate("2025-05-02T15:57:28.765Z"),
    updatedAt: ISODate("2025-05-02T15:57:54.698Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "没事，把握当下，做现在该做的事情",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("6815c45f84478f74fd6f78ee"),
    fromUserId: "68121b6606f6ed650d6b35c4",
    fromUserName: "患者1",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "头晕了好迷茫",
    isRead: true,
    createdAt: ISODate("2025-05-03T07:23:11.507Z"),
    updatedAt: ISODate("2025-05-03T07:52:22.347Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "1111111",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("6817249d042d823845d69bf8"),
    fromUserId: "68121b6606f6ed650d6b35c4",
    fromUserName: "患者1",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "睡眠质量很差",
    isRead: true,
    createdAt: ISODate("2025-05-04T08:26:05.869Z"),
    updatedAt: ISODate("2025-05-04T08:27:12.976Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "不要想过去的事情，也不要害怕未来没发生的，把现在的每一步做好，就足够了！",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("68181dcb166354369c8fa96a"),
    fromUserId: "68121b6606f6ed650d6b35c4",
    fromUserName: "患者1",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "打撒交换机考察",
    isRead: true,
    createdAt: ISODate("2025-05-05T02:09:15.75Z"),
    updatedAt: ISODate("2025-05-05T02:09:27.559Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "的飒飒大三",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("681de85819424176678b3c9c"),
    fromUserId: "68121be8d4633f7a90142dcb",
    fromUserName: "陈迅",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "身体现在很健康",
    isRead: true,
    createdAt: ISODate("2025-05-09T11:34:48.727Z"),
    updatedAt: ISODate("2025-05-09T11:35:17.82Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "那就好没问题",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("6824b24f9c783c6675565ac8"),
    fromUserId: "681825e7166354369c8fa96b",
    fromUserName: "大法师",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "有bug啊",
    isRead: true,
    createdAt: ISODate("2025-05-14T15:10:07.991Z"),
    updatedAt: ISODate("2025-05-18T16:31:07.817Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "我讨厌bug\n",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("68293ff36efa774498445986"),
    fromUserId: "68121b6606f6ed650d6b35c4",
    fromUserName: "患者1",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "bug好多\n",
    isRead: true,
    createdAt: ISODate("2025-05-18T02:03:31.774Z"),
    updatedAt: ISODate("2025-05-18T16:30:58.07Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "确实我在努力修改\n",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("682a0b18ddb17b5eb2f375ee"),
    fromUserId: "68121be8d4633f7a90142dcb",
    fromUserName: "陈迅",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "很不爽，头疼",
    isRead: true,
    createdAt: ISODate("2025-05-18T16:30:16.97Z"),
    updatedAt: ISODate("2025-05-18T16:30:50.624Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "我也是啊啊啊",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("682a0ba5ddb17b5eb2f375f0"),
    fromUserId: "68121be8d4633f7a90142dcb",
    fromUserName: "陈迅",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "我现在睡不着，焦虑的疯了",
    isRead: true,
    createdAt: ISODate("2025-05-18T16:32:37.392Z"),
    updatedAt: ISODate("2025-05-18T16:34:06.413Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "没事的，一切都会好的，你放心，你的状况很正常，但是don't worry ,Be Happy!",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("682a0cb8ddb17b5eb2f375f2"),
    fromUserId: "682a0c95ddb17b5eb2f375f1",
    fromUserName: "午夜心碎小熊",
    toUserId: "68121b5f06f6ed650d6b357f",
    toUserName: "医生2",
    content: "我超级瘦，怎么办",
    isRead: true,
    createdAt: ISODate("2025-05-18T16:37:12.753Z"),
    updatedAt: ISODate("2025-05-18T16:39:01.535Z"),
    type: "PATIENT_TO_DOCTOR",
    replyContent: "瘦很好的，你现在很苗条",
    _class: "com.example.chronicdisease.model.Message"
} ]);
db.getCollection("messages").insert([ {
    _id: ObjectId("682af1b4344cd7576b11dbd4"),
    fromUserId: "68121b6606f6ed650d6b35c4",
    fromUserName: "患者1",
    toUserId: "68121b5e06f6ed650d6b357d",
    toUserName: "医生1",
    content: "烦恼啊很麻烦",
    isRead: false,
    createdAt: ISODate("2025-05-19T08:54:12.078Z"),
    updatedAt: ISODate("2025-05-19T08:54:12.078Z"),
    type: "PATIENT_TO_DOCTOR",
    _class: "com.example.chronicdisease.model.Message"
} ]);

// ----------------------------
// Collection structure for nutrition_diets
// ----------------------------
db.getCollection("nutrition_diets").drop();
db.createCollection("nutrition_diets");

// ----------------------------
// Documents of nutrition_diets
// ----------------------------
db.getCollection("nutrition_diets").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d43"),
    title: "燕麦粥",
    content: "燕麦富含膳食纤维，有助于控制血糖和胆固醇。可以搭配水果和坚果食用。",
    category: "早餐",
    imageUrl: "/images/nutrition/oatmeal.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.NutritionDiet"
} ]);
db.getCollection("nutrition_diets").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d44"),
    title: "地中海式午餐",
    content: "以橄榄油、鱼类、蔬菜和全谷物为主，富含不饱和脂肪酸和抗氧化物质。",
    category: "午餐",
    imageUrl: "/images/nutrition/mediterranean.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.NutritionDiet"
} ]);
db.getCollection("nutrition_diets").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d45"),
    title: "清淡晚餐",
    content: "晚餐宜清淡，以蔬菜为主，搭配适量蛋白质，避免油腻和过饱。",
    category: "晚餐",
    imageUrl: "/images/nutrition/light-dinner.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.NutritionDiet"
} ]);
db.getCollection("nutrition_diets").insert([ {
    _id: ObjectId("6815ced0c31651433d5f6d46"),
    title: "坚果零食",
    content: "适量食用坚果，如杏仁、核桃等，富含健康脂肪和蛋白质，但要注意控制摄入量。",
    category: "零食",
    imageUrl: "/images/nutrition/nuts.jpg",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.NutritionDiet"
} ]);

// ----------------------------
// Collection structure for prediction_records
// ----------------------------
db.getCollection("prediction_records").drop();
db.createCollection("prediction_records");

// ----------------------------
// Documents of prediction_records
// ----------------------------
db.getCollection("prediction_records").insert([ {
    _id: ObjectId("682942a56efa774498445987"),
    userId: "681825e7166354369c8fa96b",
    healthData: {
        "姓名": "张三",
        "性别": NumberInt("1"),
        "年龄": NumberInt("68"),
        "出生日期": NumberInt("19029"),
        "吸烟": NumberInt("1"),
        "饮酒": NumberInt("1"),
        BMI: 25.390625,
        HEIGHT: NumberInt("160"),
        WEIGHT: NumberInt("65"),
        C10: "12",
        C12: NumberInt("100"),
        "C14:1": NumberInt("60"),
        C14: 3.4,
        "C16:1": NumberInt("247"),
        C16: NumberInt("135"),
        "C18:3": "15",
        "C18:2": "14",
        "C18:1": "177",
        "C18:0": "65.7",
        "C20:5": "39.5",
        "C20:4": "26.2",
        "C20:3": "10",
        "C20:2": "69",
        "C20:1": "11.6",
        C20: "2.7",
        "C22:6": "8.9",
        "C22:5": "3.8",
        "C22:4": "177",
        "C22:1": "4.55",
        C22: "52",
        "C24:1": NumberInt("27"),
        C24: "300",
        "w3/w6": "3.46",
        "Triene /Tetraene": "10391.8",
        "Total SFA": "3",
        "Total MUFA": 5.3,
        "Total PUFA": 0.85,
        "T w3": 1.54,
        "T w6": 3.22,
        "Total FA": 1.04
    },
    disease: "糖尿病",
    probability: 0.221612602472305,
    suggestion: "当前糖尿病风险较低，建议：\n1. 继续保持健康的生活方式\n2. 均衡饮食，控制糖分摄入\n3. 保持适量运动，每周至少150分钟\n4. 定期监测血糖，建议每年体检一次\n5. 保持健康体重，BMI控制在18.5-24之间",
    createdAt: ISODate("2025-05-18T02:15:01.259Z"),
    _class: "com.example.chronicdisease.model.PredictionRecord"
} ]);
db.getCollection("prediction_records").insert([ {
    _id: ObjectId("682942af6efa774498445988"),
    userId: "681825e7166354369c8fa96b",
    healthData: {
        "姓名": "开始",
        "性别": NumberInt("2"),
        "年龄": NumberInt("45"),
        "出生日期": NumberInt("29010"),
        "吸烟": NumberInt("1"),
        "饮酒": NumberInt("1"),
        BMI: 11.543,
        HEIGHT: NumberInt("170"),
        WEIGHT: NumberInt("60"),
        C10: NumberInt("121"),
        C12: NumberInt("100"),
        "C14:1": NumberInt("60"),
        C14: NumberInt("34"),
        "C16:1": NumberInt("247"),
        C16: NumberInt("135"),
        "C18:3": NumberInt("15"),
        "C18:2": NumberInt("147"),
        "C18:1": "177",
        "C18:0": "65.7",
        "C20:5": "39.5",
        "C20:4": 35.6,
        "C20:3": NumberInt("21"),
        "C20:2": NumberInt("69"),
        "C20:1": "11.6",
        C20: "2.7",
        "C22:6": "8.9",
        "C22:5": "3.8",
        "C22:4": "177",
        "C22:1": "4.55",
        C22: "52",
        "C24:1": NumberInt("27"),
        C24: "300",
        "w3/w6": "3.46",
        "Triene /Tetraene": 9891.8,
        "Total SFA": NumberInt("2"),
        "Total MUFA": 5.3,
        "Total PUFA": 0.85,
        "T w3": 1.54,
        "T w6": 3.22,
        "Total FA": 1.24
    },
    disease: "糖尿病",
    probability: 0.318004548549652,
    suggestion: "当前糖尿病风险中等，建议：\n1. 调整饮食习惯，减少高糖高脂食物\n2. 增加运动量，建议每周运动3-5次\n3. 控制体重，BMI建议控制在18.5-24之间\n4. 定期监测血糖，建议每半年体检一次\n5. 戒烟限酒，保持良好的作息习惯\n6. 如有家族史，建议咨询专业医生",
    createdAt: ISODate("2025-05-18T02:15:11.589Z"),
    _class: "com.example.chronicdisease.model.PredictionRecord"
} ]);

// ----------------------------
// Collection structure for seasonal_tips
// ----------------------------
db.getCollection("seasonal_tips").drop();
db.createCollection("seasonal_tips");

// ----------------------------
// Documents of seasonal_tips
// ----------------------------
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d2"),
    season: "spring",
    title: "过敏季节的管理",
    content: "春季花粉增多，可能加重过敏症状和哮喘。减少户外活动时间，外出后更换衣物并洗手洗脸，可有效减轻症状。",
    advice: "高血压患者请注意，某些抗过敏药物可能会影响血压，用药前请咨询医生。",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d3"),
    season: "spring",
    title: "春季饮食调整",
    content: "春季应选择新鲜蔬果，富含维生素的食物，如芦笋、菠菜和草莓等。适当增加蛋白质摄入，减少重油重盐食物。",
    advice: "糖尿病患者可适量食用春季时令蔬菜，但注意控制水果摄入量，避免血糖波动。",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d4"),
    season: "spring",
    title: "适度春季运动",
    content: "春季是开始户外活动的好时机。可选择早晨或傍晚进行散步、太极或轻度有氧运动，每周保持3-5次，每次30分钟。",
    advice: "心脏病患者应循序渐进增加运动量，避免在气温变化大的时段锻炼。",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d5"),
    season: "summer",
    title: "防暑降温措施",
    content: "夏季高温可加重心血管负担。避免在10时至16时外出，保持室内通风凉爽，多饮水但避免过凉饮品。",
    advice: "高血压患者应避免温差过大，进出空调房间时注意适应温差，预防血压波动。",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d6"),
    season: "summer",
    title: "夏季饮食建议",
    content: "夏季饮食宜清淡，可多食用苦瓜、黄瓜、西红柿等蔬菜和西瓜、梨等水果。减少高脂肪、辛辣刺激性食物。",
    advice: "糖尿病患者选择水果时，应优先考虑低糖水果如西瓜（适量）、柚子等，并计入每日碳水摄入总量。",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d7"),
    season: "summer",
    title: "夏季药物保存",
    content: "高温环境会影响药物稳定性。请将药物存放在阴凉干燥处，避免阳光直射和潮湿环境，出行时做好药物保存。",
    advice: "胰岛素等需要冷藏的药物，应放在2-8℃的冰箱中保存，但不要冷冻。",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d8"),
    season: "autumn",
    title: "秋燥与呼吸系统",
    content: "秋季气候干燥，易引起咽喉不适和呼吸道问题。建议保持室内湿度，多饮水，必要时使用加湿器。",
    advice: "慢性支气管炎患者应特别注意防燥，可通过热水泡脚、适当饮用温润茶饮等方式调节。",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395d9"),
    season: "autumn",
    title: "秋季饮食调整",
    content: "秋季应增加滋阴润肺食物，如梨、百合、银耳等。适量食用坚果类食物补充必要脂肪酸。",
    advice: "心脏病患者注意控制坚果摄入量，选择低盐或无盐坚果，每天不超过一小把。",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395da"),
    season: "autumn",
    title: "秋季情绪管理",
    content: "秋季昼夜温差大，日照减少，部分人可能出现情绪低落。保持规律作息，增加社交活动，保持积极心态。",
    advice: "长期服用抗抑郁药物的患者，在季节变化时如有情绪波动，应及时与医生沟通，切勿自行调整药物剂量。",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395db"),
    season: "winter",
    title: "防寒保暖要点",
    content: "冬季寒冷易诱发心脑血管疾病。外出前做好充分保暖，尤其是头部、颈部和足部。突然进入温暖环境时，应先解开外套适应。",
    advice: "冠心病患者冬季应减少户外活动，特别是在清晨和气温骤降时，出门前可服用备用救心药。",
    order: NumberInt("1"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbcf6d7d30dfb1395dc"),
    season: "winter",
    title: "冬季运动指南",
    content: "冬季仍需保持适当运动，可选择室内活动如太极、瑜伽等。户外运动前充分热身，选择阳光充足的午后时段。",
    advice: "关节炎患者冬季运动前可热敷关节以减轻僵硬，运动强度宜轻缓，避免剧烈运动和寒冷环境下锻炼。",
    order: NumberInt("2"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);
db.getCollection("seasonal_tips").insert([ {
    _id: ObjectId("6815ccbdf6d7d30dfb1395dd"),
    season: "winter",
    title: "节日饮食控制",
    content: "冬季节日较多，饮食易过量。建议控制节日食品摄入，保持规律三餐，避免暴饮暴食和过量饮酒。",
    advice: "糖尿病患者参加聚餐时，可提前少量进食，避免空腹赴宴；选择低糖、高纤维食物，严格控制甜点摄入。",
    order: NumberInt("3"),
    _class: "com.example.chronicdisease.model.SeasonalTip"
} ]);

// ----------------------------
// Collection structure for users
// ----------------------------
db.getCollection("users").drop();
db.createCollection("users");
db.getCollection("users").createIndex({
    username: NumberInt("1")
}, {
    name: "username",
    unique: true
});
db.getCollection("users").createIndex({
    email: NumberInt("1")
}, {
    name: "email"
});

// ----------------------------
// Documents of users
// ----------------------------
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5e06f6ed650d6b357b"),
    username: "admin",
    name: "系统管理员",
    password: "$2a$10$5kxU2pi4ZK.VJi4stQ3A5uqiiwVcfhQ8qdoVpa.IgCgprfxHrkNc2",
    email: "admin@example.com",
    phone: "13800000000",
    roles: [
        "ROLE_ADMIN"
    ],
    createdAt: ISODate("2025-04-30T12:45:18.195Z"),
    updatedAt: ISODate("2025-04-30T12:45:18.195Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5e06f6ed650d6b357c"),
    username: "doctor1",
    name: "医生1",
    password: "$2a$10$rRsmSCKZ2OOJ7WfCccrhH.rTQjel2cFrKXPeXYhqiiLBpaTH/jtrm",
    email: "doctor1@example.com",
    phone: "13800000001",
    gender: "男",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:18.627Z"),
    updatedAt: ISODate("2025-05-18T16:29:42.401Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b357e"),
    username: "doctor2",
    name: "医生2",
    password: "$2a$10$y7JkPpD3mWqkKmuDozTHh.OcJ2dOEALeth.ol4LvgW1mV9oU5VCCq",
    email: "doctor2@example.com",
    phone: "13800000002",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:19.278Z"),
    updatedAt: ISODate("2025-05-18T16:29:39.164Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3580"),
    username: "doctor3",
    name: "医生3",
    password: "$2a$10$SMdRLf/r/cF1SKQW9lo7yuyqC9u5MD/7o6VglBKnrMwOjYzJI6nqK",
    email: "doctor3@example.com",
    phone: "13800000003",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:19.492Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.492Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3582"),
    username: "doctor4",
    name: "医生4",
    password: "$2a$10$tWfP7wMt995SoI3lPlANLefxq..owxx7jAYv4kZfFum7TWbAa6ZUK",
    email: "doctor4@example.com",
    phone: "13800000004",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:19.723Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.723Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b5f06f6ed650d6b3584"),
    username: "doctor5",
    name: "医生5",
    password: "$2a$10$jZg0ZHuJAIVuH6JHiTSA5erH6p3pJs8T2.irQM8m72qSswYynoxMG",
    email: "doctor5@example.com",
    phone: "13800000005",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:19.907Z"),
    updatedAt: ISODate("2025-04-30T12:45:19.907Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b3586"),
    username: "doctor6",
    name: "医生6",
    password: "$2a$10$vvOHvDuerhF3HxXqL2EMvu5z7xr3hXcWFf73L4.sS3FayJDMOTDra",
    email: "doctor6@example.com",
    phone: "13800000006",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:20.11Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.11Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b3588"),
    username: "doctor7",
    name: "医生7",
    password: "$2a$10$vNPEkN1O/zURnjhb1vqu4.c3HYSeGEn7DBu9AmPp81o0HtLLR2kBG",
    email: "doctor7@example.com",
    phone: "13800000007",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:20.307Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.307Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358a"),
    username: "doctor8",
    name: "医生8",
    password: "$2a$10$dngJ8JNbWI00C7UMDRO3suNPuzdrCdM7rZ6AsAdcVJkIPqtmwB8uC",
    email: "doctor8@example.com",
    phone: "13800000008",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:20.493Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.493Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358c"),
    username: "doctor9",
    name: "医生9",
    password: "$2a$10$7IZ.OzMPKqMAmB5/QKFYs.FpS5Iw.0bOFYQIl4r87E0UW8v7/hsHi",
    email: "doctor9@example.com",
    phone: "13800000009",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:20.691Z"),
    updatedAt: ISODate("2025-04-30T12:45:20.691Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6006f6ed650d6b358e"),
    username: "doctor10",
    name: "医生10",
    password: "$2a$10$ZcrFLNrDmfKLOv6.Ey1ZoOvW8ESv/v3EzUdQ.FxePMtOWI/9aVWja",
    email: "doctor10@example.com",
    phone: "13800000010",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:20.881Z"),
    updatedAt: ISODate("2025-05-19T14:12:10.264Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3590"),
    username: "doctor11",
    name: "医生11",
    password: "$2a$10$VLv0bmsuMByw2ZguvSoKa.IxlRwa.BUyWeh.JWeZjsWwiUhMBoUzO",
    email: "doctor11@example.com",
    phone: "13800000011",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:21.063Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.063Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3592"),
    username: "doctor12",
    name: "医生12",
    password: "$2a$10$9YKoe5LPJ3MMxE6.Ic7VnuQoHuYX4n1UgGLU038GRY5Ptoa5m0O0m",
    email: "doctor12@example.com",
    phone: "13800000012",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:21.241Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.241Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3594"),
    username: "doctor13",
    name: "医生13",
    password: "$2a$10$LrQhvSPLqoUefCpFcq4.m.feijhqmPPRIURvLy.4XmhCA4RLr6zL6",
    email: "doctor13@example.com",
    phone: "13800000013",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:21.407Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.407Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3596"),
    username: "doctor14",
    name: "医生14",
    password: "$2a$10$5J.etUPuxqF/X2c2YxESWur6Iil6SViOdwonvNHqyF5JIBjeRReCe",
    email: "doctor14@example.com",
    phone: "13800000014",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:21.615Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.615Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6106f6ed650d6b3598"),
    username: "doctor15",
    name: "医生15",
    password: "$2a$10$stc9WY9JJlBvhbSBamE/GuOBH209f5wlT2sfSWiKXBxbIIc3/MSHC",
    email: "doctor15@example.com",
    phone: "13800000015",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:21.828Z"),
    updatedAt: ISODate("2025-04-30T12:45:21.828Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359a"),
    username: "doctor16",
    name: "医生16",
    password: "$2a$10$SCOOV9RbpmTD8/Nky2duJ.LrujSmJerUiEYIp/SxT6bElmOUhzJja",
    email: "doctor16@example.com",
    phone: "13800000016",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:22.06Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.06Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359c"),
    username: "doctor17",
    name: "医生17",
    password: "$2a$10$V1foEqrMPkMyqpjRPUK9ne/avr78G/cnV91gs3D08aJvJaNskV6tK",
    email: "doctor17@example.com",
    phone: "13800000017",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:22.254Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.254Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b359e"),
    username: "doctor18",
    name: "医生18",
    password: "$2a$10$bnLyVE22dVsu96614hzl5udKEPwGqDySPyN7VxXfe87obpB29tvFS",
    email: "doctor18@example.com",
    phone: "13800000018",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:22.43Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.43Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b35a0"),
    username: "doctor19",
    name: "医生19",
    password: "$2a$10$vJD3KlPSkC4vnZKNubk68u9XdqrgRIbPjyEHApqjpb18zphmBP/n.",
    email: "doctor19@example.com",
    phone: "13800000019",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:22.608Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.608Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6206f6ed650d6b35a2"),
    username: "doctor20",
    name: "医生20",
    password: "$2a$10$y86fIFsDNtpOvMNyizwZl.jM0l8AIOKppSGP6dlUFM8MI0ypvNhU6",
    email: "doctor20@example.com",
    phone: "13800000020",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:22.82Z"),
    updatedAt: ISODate("2025-04-30T12:45:22.82Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a4"),
    username: "doctor21",
    name: "医生21",
    password: "$2a$10$Awz/ORUwd/4fqRNh5keAveVNT/OQcYBfLTI9oPusnu7LQFGaC/Y/y",
    email: "doctor21@example.com",
    phone: "13800000021",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:23.122Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.122Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a6"),
    username: "doctor22",
    name: "医生22",
    password: "$2a$10$msbORI1KfacXH8xU6FNLUenlyiX9yjJqGMxTfU8Tg3hLoIjEIKrbm",
    email: "doctor22@example.com",
    phone: "13800000022",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:23.297Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.297Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35a8"),
    username: "doctor23",
    name: "医生23",
    password: "$2a$10$siloPo2py1h1xWPRMrRAOuk7DscbYx4KsnlBeK2bgs.NL/HYAeQiy",
    email: "doctor23@example.com",
    phone: "13800000023",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:23.481Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.481Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35aa"),
    username: "doctor24",
    name: "医生24",
    password: "$2a$10$VacaoOlfFKsVdksWNfm2Uu3wO57h8iJZAmoO2I2kfzrA.Xe7CGZZq",
    email: "doctor24@example.com",
    phone: "13800000024",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:23.664Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.664Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6306f6ed650d6b35ac"),
    username: "doctor25",
    name: "医生25",
    password: "$2a$10$nzdc/vQIMN40Hcoee/0E..yX05JTHFLOkOIgNTjS5UaT.8RNECQ/2",
    email: "doctor25@example.com",
    phone: "13800000025",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:23.834Z"),
    updatedAt: ISODate("2025-04-30T12:45:23.834Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35ae"),
    username: "doctor26",
    name: "医生26",
    password: "$2a$10$8JA6cw/8R7QKqSnbuwrSdOm0Ym5m424X8M8XxJ6kgcsjrYzxKOKxa",
    email: "doctor26@example.com",
    phone: "13800000026",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:24.01Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.01Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b0"),
    username: "doctor27",
    name: "医生27",
    password: "$2a$10$rHvjrBys0L1SNwNAB2OgIux75W.JkIb1BTjnehn5jzq5rp1Jfqsni",
    email: "doctor27@example.com",
    phone: "13800000027",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:24.191Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.191Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b2"),
    username: "doctor28",
    name: "医生28",
    password: "$2a$10$LcbhIvykH.5SrGlHP3khneN65YCaWEjkZX6nmmjYq2RdrFto/woa2",
    email: "doctor28@example.com",
    phone: "13800000028",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:24.413Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.413Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b4"),
    username: "doctor29",
    name: "医生29",
    password: "$2a$10$D6VG4UvjsvbW1CkHWayWpuY9Z2aKzBM2xg5yuSvoEfc7AP3bNxvH2",
    email: "doctor29@example.com",
    phone: "13800000029",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:24.613Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.613Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6406f6ed650d6b35b6"),
    username: "doctor30",
    name: "医生30",
    password: "$2a$10$tIB5kG4HF6yL0rJtwwx.CecN8Q1swG.3aOGNtVBSWOLnfTYnZvRnu",
    email: "doctor30@example.com",
    phone: "13800000030",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:24.793Z"),
    updatedAt: ISODate("2025-04-30T12:45:24.793Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35b8"),
    username: "doctor31",
    name: "医生31",
    password: "$2a$10$r8YR7Tq1ODXYgAvSTxZXn.SuX/TYCC9Mvko1wobjjBviP0IFPNtmy",
    email: "doctor31@example.com",
    phone: "13800000031",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.01Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.01Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35ba"),
    username: "doctor32",
    name: "医生32",
    password: "$2a$10$ouWUwXROjx11ZaZfadfOIujDc1eoRk7pjNgflBEeKXj0C6xWB3am.",
    email: "doctor32@example.com",
    phone: "13800000032",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.21Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.21Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35bc"),
    username: "doctor33",
    name: "医生33",
    password: "$2a$10$FReaAg2C7D60s7RQmzN1AOSefpJEJuoE9teiyt82AAzWOXDYQtSF6",
    email: "doctor33@example.com",
    phone: "13800000033",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.382Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.382Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35be"),
    username: "doctor34",
    name: "医生34",
    password: "$2a$10$zeEf4P3.0Z50ku32fOK2A.aGwuadRcCwqzH4pXGBkpGExpuIrhCja",
    email: "doctor34@example.com",
    phone: "13800000034",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.581Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.581Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35c0"),
    username: "doctor35",
    name: "医生35",
    password: "$2a$10$0ZyT.RWN7Wa4X/7UMZQODe/AQLiLDApeNoAh31qm7mMhgk2Ldctxm",
    email: "doctor35@example.com",
    phone: "13800000035",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.783Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.783Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6506f6ed650d6b35c2"),
    username: "doctor36",
    name: "医生36",
    password: "$2a$10$tUBNLwszUjx9RKE9zPtsYOT8UMQhFh8pIt8mYIvAZqxZIDpxPX7gW",
    email: "doctor36@example.com",
    phone: "13800000036",
    roles: [
        "ROLE_DOCTOR"
    ],
    createdAt: ISODate("2025-04-30T12:45:25.988Z"),
    updatedAt: ISODate("2025-04-30T12:45:25.988Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c4"),
    username: "user1",
    name: "患者1",
    password: "$2a$10$cQE0cG6kMrqieQMbI8QpZemWNVizqbTtCBvUVEQpqgsax38pyht9W",
    email: "user1@example.com",
    phone: "13900000001",
    age: NumberInt("21"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    boundDoctorId: "68121b5e06f6ed650d6b357d",
    createdAt: ISODate("2025-04-30T12:45:26.177Z"),
    updatedAt: ISODate("2025-05-19T08:54:24.481Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c5"),
    username: "user2",
    name: "患者2",
    password: "$2a$10$65lCccLNG9.0K1HjBTsLNuUOhEqS.Y1HCXYqk5yMDMcP7Xa85dSg2",
    email: "user2@example.com",
    phone: "13900000002",
    age: NumberInt("22"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:26.326Z"),
    updatedAt: ISODate("2025-04-30T12:45:26.326Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c6"),
    username: "user3",
    name: "患者3",
    password: "$2a$10$kHvzsHzNmCFG5oxcaf8R4el6dyoWp/FZhGF4DdyG3uYUIsOE9rIxa",
    email: "user3@example.com",
    phone: "13900000003",
    age: NumberInt("23"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:26.518Z"),
    updatedAt: ISODate("2025-04-30T12:45:26.518Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c7"),
    username: "user4",
    name: "患者4",
    password: "$2a$10$3fsWoft4cJ/XMcAhL4FEoeeV00wVFvF5fixj3xRZ2SoVOHO0EGJ5y",
    email: "user4@example.com",
    phone: "13900000004",
    age: NumberInt("24"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:26.691Z"),
    updatedAt: ISODate("2025-04-30T12:45:26.691Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6606f6ed650d6b35c8"),
    username: "user5",
    name: "患者5",
    password: "$2a$10$v1DToMm/hdiGExXqwTAtVuRVORL3Aw6wau5vc8KaSv8AuKZkJflrC",
    email: "user5@example.com",
    phone: "13900000005",
    age: NumberInt("25"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:26.861Z"),
    updatedAt: ISODate("2025-04-30T12:45:26.861Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6706f6ed650d6b35ca"),
    username: "user7",
    name: "患者7",
    password: "$2a$10$1jNi.US6oGRSykNJobP7weuGg.OF.EUiv1Kh.n00AZgH3.RL4OcPa",
    email: "user7@example.com",
    phone: "13900000007",
    age: NumberInt("27"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:27.384Z"),
    updatedAt: ISODate("2025-04-30T12:45:27.384Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6706f6ed650d6b35cb"),
    username: "user8",
    name: "患者8",
    password: "$2a$10$H4mRv3pNtjaXZIPKtGeEeux2FrgPZbh04tMgl2EjMsq0XNc6agM/2",
    email: "user8@example.com",
    phone: "13900000008",
    age: NumberInt("28"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:27.757Z"),
    updatedAt: ISODate("2025-04-30T12:45:27.757Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6806f6ed650d6b35cc"),
    username: "user9",
    name: "患者9",
    password: "$2a$10$hp9zjavXmKiYGYPtPCgFMujb0NtCzjFzgG0DZ.bpNtXzSV4LYjqju",
    email: "user9@example.com",
    phone: "13900000009",
    age: NumberInt("29"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:28.045Z"),
    updatedAt: ISODate("2025-04-30T12:45:28.045Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6806f6ed650d6b35cd"),
    username: "user10",
    name: "患者10",
    password: "$2a$10$Xgm1.bTjPt6Z1.jy9xwBFO.5CSF4e.CbYXsMnu2H/glT.aGvl9o0S",
    email: "user10@example.com",
    phone: "13900000010",
    age: NumberInt("30"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:28.296Z"),
    updatedAt: ISODate("2025-04-30T12:45:28.296Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6806f6ed650d6b35ce"),
    username: "user11",
    name: "患者11",
    password: "$2a$10$mOBv1Xo1RsWVWOmBvS/38uRztl2dQGw4YCour5KlFgOUj.BnuwrAu",
    email: "user11@example.com",
    phone: "13900000011",
    age: NumberInt("31"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:28.512Z"),
    updatedAt: ISODate("2025-04-30T12:45:28.512Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6806f6ed650d6b35cf"),
    username: "user12",
    name: "患者12",
    password: "$2a$10$8akJ.PP5u273DjxFYoafpu/EcmexYuzYdpwhOxWeP4twwyogpmc4.",
    email: "user12@example.com",
    phone: "13900000012",
    age: NumberInt("32"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:28.707Z"),
    updatedAt: ISODate("2025-04-30T12:45:28.707Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6806f6ed650d6b35d0"),
    username: "user13",
    name: "患者13",
    password: "$2a$10$QwHWuYqxVMAST7n1RYtNrOLhJxVO891LaFByUtTg.gdaF7UVwv2QC",
    email: "user13@example.com",
    phone: "13900000013",
    age: NumberInt("33"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:28.859Z"),
    updatedAt: ISODate("2025-04-30T12:45:28.859Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d1"),
    username: "user14",
    name: "患者14",
    password: "$2a$10$FDMwZNbkk8pBzRX8W3kyyORc5EhdjdWaT2iFpvdIu8k0.hkQ0ocVO",
    email: "user14@example.com",
    phone: "13900000014",
    age: NumberInt("34"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.045Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.045Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d2"),
    username: "user15",
    name: "患者15",
    password: "$2a$10$laH0eZXfEl/HEtRBrDlcG.oI0xYlfwETW99qjyoK/GO42ArCmvThS",
    email: "user15@example.com",
    phone: "13900000015",
    age: NumberInt("35"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.212Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.212Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d3"),
    username: "user16",
    name: "患者16",
    password: "$2a$10$hGW7mLLzu2McsuXIaD0ywOcuGDtQvzh1xXqxlNhCDD0NdbYK58S32",
    email: "user16@example.com",
    phone: "13900000016",
    age: NumberInt("36"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.39Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.39Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d4"),
    username: "user17",
    name: "患者17",
    password: "$2a$10$ShxCcrb/u/vWImKgVuz2BuvPU0hMfZZJdlD9hvArtX14ktaYGxbRe",
    email: "user17@example.com",
    phone: "13900000017",
    age: NumberInt("37"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.552Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.552Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d5"),
    username: "user18",
    name: "患者18",
    password: "$2a$10$bkf5RX2RIgQDSUjV0UK.FOLT7c7oiie1B0ShLD6WM5VfNiAYKyNny",
    email: "user18@example.com",
    phone: "13900000018",
    age: NumberInt("38"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.697Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.697Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6906f6ed650d6b35d6"),
    username: "user19",
    name: "患者19",
    password: "$2a$10$h1f9sMVMOzZUCns.YN3skOQMDIPLbNkzgOxwFFg3ZmZyFQatT7YM6",
    email: "user19@example.com",
    phone: "13900000019",
    age: NumberInt("39"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:29.879Z"),
    updatedAt: ISODate("2025-04-30T12:45:29.879Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35d7"),
    username: "user20",
    name: "患者20",
    password: "$2a$10$0vG5rq0mmu6l6cz5ZtqYCe9.3U2pP3OFSpIAbNH9Ff5KcEbvXGWPG",
    email: "user20@example.com",
    phone: "13900000020",
    age: NumberInt("40"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.009Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.009Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35d8"),
    username: "user21",
    name: "患者21",
    password: "$2a$10$bcZvVlMAbfDEyggBFdML7.QTQCE8Cvrg5W1ieYPxshCzCTjGaq00O",
    email: "user21@example.com",
    phone: "13900000021",
    age: NumberInt("41"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.18Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.18Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35d9"),
    username: "user22",
    name: "患者22",
    password: "$2a$10$7wSzqhwspxgZUnA98V5/h.Ig4ZEgWJa/sqfimMiHG9cTStOeC1lee",
    email: "user22@example.com",
    phone: "13900000022",
    age: NumberInt("42"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.345Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.345Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35da"),
    username: "user23",
    name: "患者23",
    password: "$2a$10$mg1f76wKeJ0AsKNuyicBv.QYF2Cjm5XNi2ARRQLHBlWLnwXxE86by",
    email: "user23@example.com",
    phone: "13900000023",
    age: NumberInt("43"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.5Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.5Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35db"),
    username: "user24",
    name: "患者24",
    password: "$2a$10$qQbLR.nZW3MkMia0J0ZeM.hTXnzYmPQmwWGl5OueYzaGlbF/S4wFW",
    email: "user24@example.com",
    phone: "13900000024",
    age: NumberInt("44"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.639Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.639Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35dc"),
    username: "user25",
    name: "患者25",
    password: "$2a$10$ctvIxaZZDwvCwgIeXVbgZeK.sqG9QVuI4b1CBJCmyISpWBeSiWLqO",
    email: "user25@example.com",
    phone: "13900000025",
    age: NumberInt("45"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.782Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.782Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6a06f6ed650d6b35dd"),
    username: "user26",
    name: "患者26",
    password: "$2a$10$Ob/FR.Gv19kStbs.qeNdfuGG0/pQ5kDR2GWvleQ/7.4EEQE.TuEb.",
    email: "user26@example.com",
    phone: "13900000026",
    age: NumberInt("46"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:30.92Z"),
    updatedAt: ISODate("2025-04-30T12:45:30.92Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6b06f6ed650d6b35de"),
    username: "user27",
    name: "患者27",
    password: "$2a$10$y0tCQAYIjhnhwXGEgb2UDe2nS38Y3a5/CDnJslOEJyuHKNu52MOIe",
    email: "user27@example.com",
    phone: "13900000027",
    age: NumberInt("47"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:31.164Z"),
    updatedAt: ISODate("2025-04-30T12:45:31.164Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6b06f6ed650d6b35df"),
    username: "user28",
    name: "患者28",
    password: "$2a$10$fX/r.J.h5qE3o4P/6ckALOYtVSmr.uOu3id2.DOZfxNUouc4W.Pru",
    email: "user28@example.com",
    phone: "13900000028",
    age: NumberInt("48"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:31.344Z"),
    updatedAt: ISODate("2025-04-30T12:45:31.344Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6b06f6ed650d6b35e0"),
    username: "user29",
    name: "患者29",
    password: "$2a$10$eGebFTLFXgJ8xYq02E65n.Zm8kMk2qlf4ZH7CC8J6vl5AapNZOpEq",
    email: "user29@example.com",
    phone: "13900000029",
    age: NumberInt("49"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:31.51Z"),
    updatedAt: ISODate("2025-04-30T12:45:31.51Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6b06f6ed650d6b35e1"),
    username: "user30",
    name: "患者30",
    password: "$2a$10$boXO2jHcVYyywycsaB2V5u2E9WHTt2mxygC74L7QfOuioJZim27Gy",
    email: "user30@example.com",
    phone: "13900000030",
    age: NumberInt("50"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:31.707Z"),
    updatedAt: ISODate("2025-04-30T12:45:31.707Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6b06f6ed650d6b35e2"),
    username: "user31",
    name: "患者31",
    password: "$2a$10$C2OBSxs5Y0ddN3Qm6HDWxuVV.dAY8rDCR6o9RjMenBWyhXVtHgCQW",
    email: "user31@example.com",
    phone: "13900000031",
    age: NumberInt("51"),
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:31.917Z"),
    updatedAt: ISODate("2025-04-30T12:45:31.917Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121b6c06f6ed650d6b35e3"),
    username: "user32",
    name: "患者32",
    password: "$2a$10$EvzCNxEl9mTxmdsQJwFTWuxuaYBG72tOlKYBh7v94Kz4Y7Eb5bCem",
    email: "user32@example.com",
    phone: "13900000032",
    age: NumberInt("52"),
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-04-30T12:45:32.065Z"),
    updatedAt: ISODate("2025-04-30T12:45:32.065Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("68121be8d4633f7a90142dcb"),
    username: "topcx",
    name: "陈迅",
    password: "$2a$10$an2M8IF5XBPXNsQmGxj/xeAtAjboQIUZzIWCE3fUdUFl6eGcawAOa",
    email: "1677940751@qq.com",
    phone: "18351589652",
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    boundDoctorId: "68121b5e06f6ed650d6b357d",
    createdAt: ISODate("2025-04-30T12:47:36.951Z"),
    updatedAt: ISODate("2025-05-19T16:28:06.833Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("6814a301b27e3d5da142da32"),
    username: "naln",
    name: "纳兰医生",
    password: "$2a$10$tks.3a8Baf0saaAViXotSuvNecZlFpfcDPo9VFrz8J04auRWMGeyi",
    phone: "13122222222",
    roles: [
        "ROLE_USER"
    ],
    createdAt: ISODate("2025-05-02T10:48:33.201Z"),
    updatedAt: ISODate("2025-05-02T10:48:33.201Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("681825e7166354369c8fa96b"),
    username: "q1w2e3",
    name: "大法师",
    password: "$2a$10$ZlDbLvF04Up3LtouIW3q6.njsPAzqgZzFN7/JZ6t.oNSrmqLs9z7C",
    email: "",
    phone: "13400033333",
    gender: "男",
    roles: [
        "ROLE_USER"
    ],
    boundDoctorId: "68121b5e06f6ed650d6b357d",
    createdAt: ISODate("2025-05-05T02:43:51.733Z"),
    updatedAt: ISODate("2025-05-18T02:06:44.624Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
db.getCollection("users").insert([ {
    _id: ObjectId("682a0c95ddb17b5eb2f375f1"),
    username: "zxy",
    name: "午夜心碎小熊",
    password: "$2a$10$IxeWCvBVtJYW8cVELAi80uocY..wJ0AUwOCOwkdYwQM2/yVn8P.jC",
    email: "",
    phone: "13400000007",
    gender: "女",
    roles: [
        "ROLE_USER"
    ],
    boundDoctorId: "68121b5f06f6ed650d6b357f",
    createdAt: ISODate("2025-05-18T16:36:37.515Z"),
    updatedAt: ISODate("2025-05-18T16:39:26.754Z"),
    active: true,
    _class: "com.example.chronicdisease.model.User"
} ]);
