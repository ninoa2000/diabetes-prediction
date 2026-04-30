package com.example.chronicdisease.config;

import com.example.chronicdisease.model.ChronicDisease;
import com.example.chronicdisease.model.LatestResearch;
import com.example.chronicdisease.model.SeasonalTip;
import com.example.chronicdisease.model.HealthHabit;
import com.example.chronicdisease.model.NutritionDiet;
import com.example.chronicdisease.model.ChronicDiseaseMedication;
import com.example.chronicdisease.repository.ChronicDiseaseRepository;
import com.example.chronicdisease.repository.LatestResearchRepository;
import com.example.chronicdisease.repository.SeasonalTipRepository;
import com.example.chronicdisease.repository.HealthHabitRepository;
import com.example.chronicdisease.repository.NutritionDietRepository;
import com.example.chronicdisease.repository.ChronicDiseaseMedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private ChronicDiseaseRepository chronicDiseaseRepository;

    @Autowired
    private LatestResearchRepository latestResearchRepository;
    
    @Autowired
    private SeasonalTipRepository seasonalTipRepository;
    
    @Autowired
    private HealthHabitRepository healthHabitRepository;
    
    @Autowired
    private NutritionDietRepository nutritionDietRepository;
    
    @Autowired
    private ChronicDiseaseMedicationRepository chronicDiseaseMedicationRepository;
    
    @Override
    public void run(String... args) {
        // 初始化慢性病数据
        if (chronicDiseaseRepository.count() == 0) {
            // 糖尿病
            ChronicDisease diabetes = new ChronicDisease();
            diabetes.setName("糖尿病");
            diabetes.setDescription("糖尿病是一种以高血糖为特征的代谢性疾病，长期存在的高血糖会导致各种组织，特别是眼、肾、心脏、血管、神经的慢性损害、功能障碍。");
            diabetes.setSymptoms("多饮、多食、多尿、体重减轻、疲劳、视力模糊、伤口愈合缓慢");
            diabetes.setCauses("遗传因素、环境因素、生活方式、肥胖、缺乏运动、高血压、高血脂等");
            diabetes.setPrevention("健康饮食、规律运动、控制体重、定期体检、戒烟限酒、保持良好心态");
            diabetes.setTreatment("药物治疗、饮食控制、运动疗法、血糖监测、胰岛素治疗、并发症防治");
            diabetes.setRiskFactors(Arrays.asList("肥胖", "家族史", "缺乏运动", "高血压", "高血脂", "年龄大于45岁", "妊娠糖尿病史"));
            diabetes.setImageUrl("/images/diabetes.jpg");
            diabetes.setOrder(1);
            chronicDiseaseRepository.save(diabetes);
            
            // 高血压
            ChronicDisease hypertension = new ChronicDisease();
            hypertension.setName("高血压");
            hypertension.setDescription("高血压是一种以动脉血压持续升高为特征的疾病，是心脑血管疾病的主要危险因素。");
            hypertension.setSymptoms("头痛、头晕、心悸、耳鸣、失眠、注意力不集中、记忆力减退");
            hypertension.setCauses("遗传因素、高盐饮食、肥胖、压力、缺乏运动、吸烟、饮酒、年龄增长");
            hypertension.setPrevention("低盐饮食、控制体重、戒烟限酒、规律运动、保持良好心态、定期监测血压");
            hypertension.setTreatment("药物治疗、生活方式调整、血压监测、并发症防治");
            hypertension.setRiskFactors(Arrays.asList("高龄", "家族史", "高盐饮食", "肥胖", "缺乏运动", "吸烟", "饮酒", "压力大"));
            hypertension.setImageUrl("/images/hypertension.jpg");
            hypertension.setOrder(2);
            chronicDiseaseRepository.save(hypertension);
            
            // 心脏病
            ChronicDisease heartDisease = new ChronicDisease();
            heartDisease.setName("心脏病");
            heartDisease.setDescription("心脏病是一类影响心脏结构和功能的疾病，包括冠心病、心肌病、心律失常等。");
            heartDisease.setSymptoms("胸痛、胸闷、心悸、气短、疲劳、头晕、晕厥、下肢水肿");
            heartDisease.setCauses("高血压、高血脂、糖尿病、吸烟、肥胖、缺乏运动、压力、遗传因素");
            heartDisease.setPrevention("控制血压、血脂、血糖、戒烟、健康饮食、规律运动、控制体重、保持良好心态");
            heartDisease.setTreatment("药物治疗、手术治疗、生活方式调整、康复训练、定期随访");
            heartDisease.setRiskFactors(Arrays.asList("高血压", "高血脂", "糖尿病", "吸烟", "肥胖", "缺乏运动", "家族史", "年龄增长"));
            heartDisease.setImageUrl("/images/heart-disease.jpg");
            heartDisease.setOrder(3);
            chronicDiseaseRepository.save(heartDisease);
        }
        
        // 初始化最新研究数据
        if (latestResearchRepository.count() == 0) {
            // 研究1
            LatestResearch research1 = new LatestResearch();
            research1.setTitle("新型糖尿病治疗药物研究进展");
            research1.setContent("最新研究表明，新型GLP-1受体激动剂在控制血糖和减轻体重方面效果显著，可降低心血管事件风险。");
            research1.setSource("中华糖尿病杂志");
            research1.setAuthor("张教授");
            research1.setPublishDate(new Date());
            research1.setImageUrl("/images/research1.jpg");
            research1.setLink("https://example.com/research1");
            research1.setOrder(1);
            latestResearchRepository.save(research1);
            
            // 研究2
            LatestResearch research2 = new LatestResearch();
            research2.setTitle("高血压患者生活方式干预研究");
            research2.setContent("一项为期5年的研究表明，综合生活方式干预可显著降低高血压患者的血压水平，减少心血管事件发生率。");
            research2.setSource("中国高血压杂志");
            research2.setAuthor("李教授");
            research2.setPublishDate(new Date());
            research2.setImageUrl("/images/research2.jpg");
            research2.setLink("https://example.com/research2");
            research2.setOrder(2);
            latestResearchRepository.save(research2);
            
            // 研究3
            LatestResearch research3 = new LatestResearch();
            research3.setTitle("心脏病早期诊断新方法");
            research3.setContent("研究人员开发出一种新型生物标志物检测方法，可提前预测心脏病风险，准确率达90%以上。");
            research3.setSource("中国心血管病杂志");
            research3.setAuthor("王教授");
            research3.setPublishDate(new Date());
            research3.setImageUrl("/images/research3.jpg");
            research3.setLink("https://example.com/research3");
            research3.setOrder(3);
            latestResearchRepository.save(research3);
            
            // 研究4
            LatestResearch research4 = new LatestResearch();
            research4.setTitle("糖尿病并发症防治新进展");
            research4.setContent("最新研究发现，严格控制血糖可显著降低糖尿病并发症风险，特别是视网膜病变和肾病。");
            research4.setSource("中华糖尿病杂志");
            research4.setAuthor("赵教授");
            research4.setPublishDate(new Date());
            research4.setImageUrl("/images/research4.jpg");
            research4.setLink("https://example.com/research4");
            research4.setOrder(4);
            latestResearchRepository.save(research4);
            
            // 研究5
            LatestResearch research5 = new LatestResearch();
            research5.setTitle("高血压患者运动处方研究");
            research5.setContent("研究表明，规律的有氧运动可显著降低高血压患者的血压水平，改善血管功能。");
            research5.setSource("中国高血压杂志");
            research5.setAuthor("刘教授");
            research5.setPublishDate(new Date());
            research5.setImageUrl("/images/research5.jpg");
            research5.setLink("https://example.com/research5");
            research5.setOrder(5);
            latestResearchRepository.save(research5);
            
            // 研究6
            LatestResearch research6 = new LatestResearch();
            research6.setTitle("心脏病康复治疗新方法");
            research6.setContent("新型心脏康复方案可显著提高心脏病患者的生活质量和运动能力，降低再入院率。");
            research6.setSource("中国心血管病杂志");
            research6.setAuthor("陈教授");
            research6.setPublishDate(new Date());
            research6.setImageUrl("/images/research6.jpg");
            research6.setLink("https://example.com/research6");
            research6.setOrder(6);
            latestResearchRepository.save(research6);
            
            // 研究7
            LatestResearch research7 = new LatestResearch();
            research7.setTitle("糖尿病营养治疗新进展");
            research7.setContent("个性化营养治疗方案可显著改善糖尿病患者的血糖控制和生活质量。");
            research7.setSource("中华糖尿病杂志");
            research7.setAuthor("孙教授");
            research7.setPublishDate(new Date());
            research7.setImageUrl("/images/research7.jpg");
            research7.setLink("https://example.com/research7");
            research7.setOrder(7);
            latestResearchRepository.save(research7);
            
            // 研究8
            LatestResearch research8 = new LatestResearch();
            research8.setTitle("高血压患者心理干预研究");
            research8.setContent("心理干预可显著改善高血压患者的心理状态和血压控制效果。");
            research8.setSource("中国高血压杂志");
            research8.setAuthor("周教授");
            research8.setPublishDate(new Date());
            research8.setImageUrl("/images/research8.jpg");
            research8.setLink("https://example.com/research8");
            research8.setOrder(8);
            latestResearchRepository.save(research8);
            
            // 研究9
            LatestResearch research9 = new LatestResearch();
            research9.setTitle("心脏病患者远程监护研究");
            research9.setContent("远程监护系统可显著提高心脏病患者的治疗依从性和生活质量。");
            research9.setSource("中国心血管病杂志");
            research9.setAuthor("吴教授");
            research9.setPublishDate(new Date());
            research9.setImageUrl("/images/research9.jpg");
            research9.setLink("https://example.com/research9");
            research9.setOrder(9);
            latestResearchRepository.save(research9);
            
            // 研究10
            LatestResearch research10 = new LatestResearch();
            research10.setTitle("糖尿病并发症早期筛查新方法");
            research10.setContent("新型筛查方法可早期发现糖尿病并发症，提高治疗效果。");
            research10.setSource("中华糖尿病杂志");
            research10.setAuthor("郑教授");
            research10.setPublishDate(new Date());
            research10.setImageUrl("/images/research10.jpg");
            research10.setLink("https://example.com/research10");
            research10.setOrder(10);
            latestResearchRepository.save(research10);
            
            // 研究11
            LatestResearch research11 = new LatestResearch();
            research11.setTitle("高血压患者药物治疗新进展");
            research11.setContent("新型降压药物可显著提高血压控制率，减少不良反应。");
            research11.setSource("中国高血压杂志");
            research11.setAuthor("马教授");
            research11.setPublishDate(new Date());
            research11.setImageUrl("/images/research11.jpg");
            research11.setLink("https://example.com/research11");
            research11.setOrder(11);
            latestResearchRepository.save(research11);
            
            // 研究12
            LatestResearch research12 = new LatestResearch();
            research12.setTitle("心脏病患者生活方式干预研究");
            research12.setContent("综合生活方式干预可显著改善心脏病患者的生活质量和预后。");
            research12.setSource("中国心血管病杂志");
            research12.setAuthor("黄教授");
            research12.setPublishDate(new Date());
            research12.setImageUrl("/images/research12.jpg");
            research12.setLink("https://example.com/research12");
            research12.setOrder(12);
            latestResearchRepository.save(research12);
        }
        
        // 初始化季节性健康提示数据
        if (seasonalTipRepository.count() == 0) {
            // 春季提示
            SeasonalTip springTip1 = new SeasonalTip();
            springTip1.setSeason("spring");
            springTip1.setTitle("过敏季节的管理");
            springTip1.setContent("春季花粉增多，可能加重过敏症状和哮喘。减少户外活动时间，外出后更换衣物并洗手洗脸，可有效减轻症状。");
            springTip1.setAdvice("高血压患者请注意，某些抗过敏药物可能会影响血压，用药前请咨询医生。");
            springTip1.setOrder(1);
            seasonalTipRepository.save(springTip1);
            
            SeasonalTip springTip2 = new SeasonalTip();
            springTip2.setSeason("spring");
            springTip2.setTitle("春季饮食调整");
            springTip2.setContent("春季应选择新鲜蔬果，富含维生素的食物，如芦笋、菠菜和草莓等。适当增加蛋白质摄入，减少重油重盐食物。");
            springTip2.setAdvice("糖尿病患者可适量食用春季时令蔬菜，但注意控制水果摄入量，避免血糖波动。");
            springTip2.setOrder(2);
            seasonalTipRepository.save(springTip2);
            
            SeasonalTip springTip3 = new SeasonalTip();
            springTip3.setSeason("spring");
            springTip3.setTitle("适度春季运动");
            springTip3.setContent("春季是开始户外活动的好时机。可选择早晨或傍晚进行散步、太极或轻度有氧运动，每周保持3-5次，每次30分钟。");
            springTip3.setAdvice("心脏病患者应循序渐进增加运动量，避免在气温变化大的时段锻炼。");
            springTip3.setOrder(3);
            seasonalTipRepository.save(springTip3);
            
            // 夏季提示
            SeasonalTip summerTip1 = new SeasonalTip();
            summerTip1.setSeason("summer");
            summerTip1.setTitle("防暑降温措施");
            summerTip1.setContent("夏季高温可加重心血管负担。避免在10时至16时外出，保持室内通风凉爽，多饮水但避免过凉饮品。");
            summerTip1.setAdvice("高血压患者应避免温差过大，进出空调房间时注意适应温差，预防血压波动。");
            summerTip1.setOrder(1);
            seasonalTipRepository.save(summerTip1);
            
            SeasonalTip summerTip2 = new SeasonalTip();
            summerTip2.setSeason("summer");
            summerTip2.setTitle("夏季饮食建议");
            summerTip2.setContent("夏季饮食宜清淡，可多食用苦瓜、黄瓜、西红柿等蔬菜和西瓜、梨等水果。减少高脂肪、辛辣刺激性食物。");
            summerTip2.setAdvice("糖尿病患者选择水果时，应优先考虑低糖水果如西瓜（适量）、柚子等，并计入每日碳水摄入总量。");
            summerTip2.setOrder(2);
            seasonalTipRepository.save(summerTip2);
            
            SeasonalTip summerTip3 = new SeasonalTip();
            summerTip3.setSeason("summer");
            summerTip3.setTitle("夏季药物保存");
            summerTip3.setContent("高温环境会影响药物稳定性。请将药物存放在阴凉干燥处，避免阳光直射和潮湿环境，出行时做好药物保存。");
            summerTip3.setAdvice("胰岛素等需要冷藏的药物，应放在2-8℃的冰箱中保存，但不要冷冻。");
            summerTip3.setOrder(3);
            seasonalTipRepository.save(summerTip3);
            
            // 秋季提示
            SeasonalTip autumnTip1 = new SeasonalTip();
            autumnTip1.setSeason("autumn");
            autumnTip1.setTitle("秋燥与呼吸系统");
            autumnTip1.setContent("秋季气候干燥，易引起咽喉不适和呼吸道问题。建议保持室内湿度，多饮水，必要时使用加湿器。");
            autumnTip1.setAdvice("慢性支气管炎患者应特别注意防燥，可通过热水泡脚、适当饮用温润茶饮等方式调节。");
            autumnTip1.setOrder(1);
            seasonalTipRepository.save(autumnTip1);
            
            SeasonalTip autumnTip2 = new SeasonalTip();
            autumnTip2.setSeason("autumn");
            autumnTip2.setTitle("秋季饮食调整");
            autumnTip2.setContent("秋季应增加滋阴润肺食物，如梨、百合、银耳等。适量食用坚果类食物补充必要脂肪酸。");
            autumnTip2.setAdvice("心脏病患者注意控制坚果摄入量，选择低盐或无盐坚果，每天不超过一小把。");
            autumnTip2.setOrder(2);
            seasonalTipRepository.save(autumnTip2);
            
            SeasonalTip autumnTip3 = new SeasonalTip();
            autumnTip3.setSeason("autumn");
            autumnTip3.setTitle("秋季情绪管理");
            autumnTip3.setContent("秋季昼夜温差大，日照减少，部分人可能出现情绪低落。保持规律作息，增加社交活动，保持积极心态。");
            autumnTip3.setAdvice("长期服用抗抑郁药物的患者，在季节变化时如有情绪波动，应及时与医生沟通，切勿自行调整药物剂量。");
            autumnTip3.setOrder(3);
            seasonalTipRepository.save(autumnTip3);
            
            // 冬季提示
            SeasonalTip winterTip1 = new SeasonalTip();
            winterTip1.setSeason("winter");
            winterTip1.setTitle("防寒保暖要点");
            winterTip1.setContent("冬季寒冷易诱发心脑血管疾病。外出前做好充分保暖，尤其是头部、颈部和足部。突然进入温暖环境时，应先解开外套适应。");
            winterTip1.setAdvice("冠心病患者冬季应减少户外活动，特别是在清晨和气温骤降时，出门前可服用备用救心药。");
            winterTip1.setOrder(1);
            seasonalTipRepository.save(winterTip1);
            
            SeasonalTip winterTip2 = new SeasonalTip();
            winterTip2.setSeason("winter");
            winterTip2.setTitle("冬季运动指南");
            winterTip2.setContent("冬季仍需保持适当运动，可选择室内活动如太极、瑜伽等。户外运动前充分热身，选择阳光充足的午后时段。");
            winterTip2.setAdvice("关节炎患者冬季运动前可热敷关节以减轻僵硬，运动强度宜轻缓，避免剧烈运动和寒冷环境下锻炼。");
            winterTip2.setOrder(2);
            seasonalTipRepository.save(winterTip2);
            
            SeasonalTip winterTip3 = new SeasonalTip();
            winterTip3.setSeason("winter");
            winterTip3.setTitle("节日饮食控制");
            winterTip3.setContent("冬季节日较多，饮食易过量。建议控制节日食品摄入，保持规律三餐，避免暴饮暴食和过量饮酒。");
            winterTip3.setAdvice("糖尿病患者参加聚餐时，可提前少量进食，避免空腹赴宴；选择低糖、高纤维食物，严格控制甜点摄入。");
            winterTip3.setOrder(3);
            seasonalTipRepository.save(winterTip3);
        }
        
        // 初始化健康习惯数据
        if (healthHabitRepository.count() == 0) {
            // 运动类
            HealthHabit exercise1 = new HealthHabit();
            exercise1.setTitle("每日步行");
            exercise1.setContent("每天坚持步行30分钟，可以改善心肺功能，降低血压和血糖水平。建议选择空气清新的公园或步道。");
            exercise1.setCategory("运动");
            exercise1.setImageUrl("/images/health-habits/walking.jpg");
            exercise1.setOrder(1);
            healthHabitRepository.save(exercise1);
            
            HealthHabit exercise2 = new HealthHabit();
            exercise2.setTitle("太极养生");
            exercise2.setContent("太极拳动作缓慢柔和，适合中老年人练习，可以改善平衡能力，缓解关节疼痛。");
            exercise2.setCategory("运动");
            exercise2.setImageUrl("/images/health-habits/taichi.jpg");
            exercise2.setOrder(2);
            healthHabitRepository.save(exercise2);
            
            // 睡眠类
            HealthHabit sleep1 = new HealthHabit();
            sleep1.setTitle("规律作息");
            sleep1.setContent("保持固定的睡眠时间，建议晚上10点前入睡，保证7-8小时的睡眠时间。睡前避免使用电子设备。");
            sleep1.setCategory("睡眠");
            sleep1.setImageUrl("/images/health-habits/sleep.jpg");
            sleep1.setOrder(1);
            healthHabitRepository.save(sleep1);
            
            // 心理类
            HealthHabit mental1 = new HealthHabit();
            mental1.setTitle("正念冥想");
            mental1.setContent("每天进行10-15分钟的正念冥想，可以帮助缓解压力，改善情绪，提高生活质量。");
            mental1.setCategory("心理");
            mental1.setImageUrl("/images/health-habits/meditation.jpg");
            mental1.setOrder(1);
            healthHabitRepository.save(mental1);
        }
        
        // 初始化营养饮食数据
        if (nutritionDietRepository.count() == 0) {
            // 早餐类
            NutritionDiet breakfast1 = new NutritionDiet();
            breakfast1.setTitle("燕麦粥");
            breakfast1.setContent("燕麦富含膳食纤维，有助于控制血糖和胆固醇。可以搭配水果和坚果食用。");
            breakfast1.setCategory("早餐");
            breakfast1.setImageUrl("/images/nutrition/oatmeal.jpg");
            breakfast1.setOrder(1);
            nutritionDietRepository.save(breakfast1);
            
            // 午餐类
            NutritionDiet lunch1 = new NutritionDiet();
            lunch1.setTitle("地中海式午餐");
            lunch1.setContent("以橄榄油、鱼类、蔬菜和全谷物为主，富含不饱和脂肪酸和抗氧化物质。");
            lunch1.setCategory("午餐");
            lunch1.setImageUrl("/images/nutrition/mediterranean.jpg");
            lunch1.setOrder(1);
            nutritionDietRepository.save(lunch1);
            
            // 晚餐类
            NutritionDiet dinner1 = new NutritionDiet();
            dinner1.setTitle("清淡晚餐");
            dinner1.setContent("晚餐宜清淡，以蔬菜为主，搭配适量蛋白质，避免油腻和过饱。");
            dinner1.setCategory("晚餐");
            dinner1.setImageUrl("/images/nutrition/light-dinner.jpg");
            dinner1.setOrder(1);
            nutritionDietRepository.save(dinner1);
            
            // 零食类
            NutritionDiet snack1 = new NutritionDiet();
            snack1.setTitle("坚果零食");
            snack1.setContent("适量食用坚果，如杏仁、核桃等，富含健康脂肪和蛋白质，但要注意控制摄入量。");
            snack1.setCategory("零食");
            snack1.setImageUrl("/images/nutrition/nuts.jpg");
            snack1.setOrder(1);
            nutritionDietRepository.save(snack1);
        }
        
        // 初始化慢性病常用药数据
        if (chronicDiseaseMedicationRepository.count() == 0) {
            // 高血压药物
            ChronicDiseaseMedication hypertension1 = new ChronicDiseaseMedication();
            hypertension1.setName("氨氯地平");
            hypertension1.setDisease("高血压");
            hypertension1.setUsage("每日一次，每次5-10mg，早餐后服用");
            hypertension1.setSideEffects("可能引起头痛、水肿、心悸等");
            hypertension1.setPrecautions("避免与葡萄柚汁同服，定期监测血压");
            hypertension1.setImageUrl("/images/medications/amlodipine.jpg");
            hypertension1.setOrder(1);
            chronicDiseaseMedicationRepository.save(hypertension1);
            
            // 糖尿病药物
            ChronicDiseaseMedication diabetes1 = new ChronicDiseaseMedication();
            diabetes1.setName("二甲双胍");
            diabetes1.setDisease("糖尿病");
            diabetes1.setUsage("每日2-3次，每次500-850mg，餐后服用");
            diabetes1.setSideEffects("可能引起胃肠道不适，如恶心、腹泻等");
            diabetes1.setPrecautions("肾功能不全者慎用，定期监测血糖");
            diabetes1.setImageUrl("/images/medications/metformin.jpg");
            diabetes1.setOrder(1);
            chronicDiseaseMedicationRepository.save(diabetes1);
            
            // 冠心病药物
            ChronicDiseaseMedication coronary1 = new ChronicDiseaseMedication();
            coronary1.setName("阿司匹林");
            coronary1.setDisease("冠心病");
            coronary1.setUsage("每日一次，每次75-100mg，餐后服用");
            coronary1.setSideEffects("可能引起胃肠道不适，增加出血风险");
            coronary1.setPrecautions("有出血倾向者慎用，定期检查凝血功能");
            coronary1.setImageUrl("/images/medications/aspirin.jpg");
            coronary1.setOrder(1);
            chronicDiseaseMedicationRepository.save(coronary1);
        }
    }
} 