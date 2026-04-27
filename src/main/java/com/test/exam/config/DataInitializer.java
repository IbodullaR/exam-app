package com.test.exam.config;

import com.test.exam.entity.Question;
import com.test.exam.entity.Subject;
import com.test.exam.repository.QuestionRepository;
import com.test.exam.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (subjectRepository.count() > 0) return; // already initialized

        Subject s = new Subject();
        s.setName("Marketing asoslari");
        subjectRepository.save(s);

        addQ(s, "Marketing atamasi fanga qachon kiritildi?", "1904","1905","1902","1901","1902",1);
        addQ(s, "Marketing nima?", "Bozorda maxsulotni sotishdir","Talabni kondirishga karatilgan faoliyatdir","Extiyojni xisobga olmagan xolda ishlab chiqarilgan maxsulotni sotish","Iste'molchiga nima kerak bulsa ushani ishlab chiqarash va sotish","Iste'molchiga nima kerak bulsa ushani ishlab chiqarash va sotish",1);
        addQ(s, "Marketing qanday tizimni ifodalaydi?", "Sotish","Ishlab chiqarish","Ayirboshlash","Ishlab chiqarish va sotish","Ishlab chiqarish va sotish",1);
        addQ(s, "Bozor nima?", "U tovarlarni joylashtiradigan joydir","U mavjud va potentsial xaridorlar yigindisidir","U sotuvchilar ishtirok etadagan joydir","U xaridorlar bilan raqobatchilar yigiladigan joy","U mavjud va potentsial xaridorlar yigindisidir",1);
        addQ(s, "Ehtiyoj nima?", "Individ shaxsning madaniy darajasiga asosan spetsifik shaklga kirgan muhtojlikdir","U shaxsning biron-bir narsani talab qilishi","Kishilarni biron-bir narsaga bulgan munosabati","U kishining biron-bir narsaga bulgan intilishi","Individ shaxsning madaniy darajasiga asosan spetsifik shaklga kirgan muhtojlikdir",1);
        addQ(s, "Raqobat nima?", "Xaridorlar urtasidagi kurash","U aynan bir maqsadga erishish uchun bir qancha shaxslar urtasidagi kurash faoliyatidir","Sotuvchilar hamda xaridorlar kurashi","Iste'molchilar urtasidagi kurash faoliyati","U aynan bir maqsadga erishish uchun bir qancha shaxslar urtasidagi kurash faoliyatidir",1);
        addQ(s, "Muhtojlik nima?", "Tovarlarni etishmasligini his qilish","Kishiga biron-bir narsani etishmasligini his qilishdir","Pul etishmasligini his etish","Xizmatlarni etishmasligini his qilish","Kishiga biron-bir narsani etishmasligini his qilishdir",1);
        addQ(s, "Bozorni segmentlash nima?", "Raqobatchilarni guruxlarga bulish","Iste'molchilarni guruxga bulishdir","Bozordan chiqib ketish","Tovarlarni guruxga bulish","Iste'molchilarni guruxga bulishdir",2);
        addQ(s, "Motivasiya deganda nimani tushunasiz?", "Kishilarni xulq-atvorini urganish","Kishilar faoliyatini ruxiy yullar bilan maqsadga muvofiq yunaltirish","Tanlashni amalga oshirishdagi xaridorlik xulq-atvori","Xodimlarni jonli mexnat faoliyatiga ragbatlantiruvchi vositalar","Kishilar faoliyatini ruxiy yullar bilan maqsadga muvofiq yunaltirish",2);
        addQ(s, "Tovarni ortacha bozor narxidan arzon sotish nima deyiladi?", "Xolding","Brending","Demping","Tyuning","Demping",3);
        addQ(s, "Remarketing qachon qollaniladi?", "Taklif kuchayganda","Talab kuchayganda","Taklif pasayganda","Talab pasayganda","Talab pasayganda",3);
        addQ(s, "Marketing kompleksi nima?", "Bu marketingni tovar, narx elementlari yigindisidir","Bu bozordagi taqsimot, kommunikatsiya kanallari yigindisidir","Bu bozorga tovarni etkazib berish uchun zarur bulgan marketing elementlarini optimal kombinatsiyasidir","Marketingni tovar va siljitish elementlari yigindisidir","Bu bozorga tovarni etkazib berish uchun zarur bulgan marketing elementlarini optimal kombinatsiyasidir",1);
        addQ(s, "Narx siyosati nima?", "Tovar siyosati","Kommunikatsiya siyosati","Narx siyosatdir","Bozor","Narx siyosatdir",1);
        addQ(s, "Taqsimot kanali nima?", "Bu reklamani tarqatish usuli","Tovarni iste'molchiga etkazishni ta'minlovchi tashkilot yoki shaxslar yigindisidir","Tovarni transportirovka usuli","Bozor","Tovarni iste'molchiga etkazishni ta'minlovchi tashkilot yoki shaxslar yigindisidir",1);
        addQ(s, "Diversifikasiyalash nima?", "Yangi bozorlarni egallab olish maqsadida ishlab chiqarishning bir-biri bilan boglik bulmagan ikki yoki undan ortiq turini kengaytirish","Modernizasiya","Tyuning","Kommunikasiya","Yangi bozorlarni egallab olish maqsadida ishlab chiqarishning bir-biri bilan boglik bulmagan ikki yoki undan ortiq turini kengaytirish",1);
        addQ(s, "Bozorni segmentlash nima? (2)", "Bozorni segmentlashdir","Reklama","Kotirovkalash","Demping","Bozorni segmentlashdir",2);
        addQ(s, "Elektron tijorat nima?", "Chakana savdo","Auksion","Elektron tijoratdir","Ulgurji savdo","Elektron tijoratdir",3);
        addQ(s, "Reklama nima?", "Juda katta talab","Ikki taraflama kommunikasiya","Ommaviy bulgan kommunikasiyasilar","Bu shaxsiy bulmagan kommunikasiya","Ommaviy bulgan kommunikasiyasilar",2);
        addQ(s, "SWOT tahlil deganda nimani tushunasiz?", "Qulayliklar tahdidlar","Korxonaning kuchli va ojiz tomonlari, imkoniyatlari, qulayliklari va tahdidlar strategik audit yuli bilan olib boriladi","Korxonaning kuchli, ojiz tomonlari","Korxonaning ojiz tomonlari","Korxonaning kuchli va ojiz tomonlari, imkoniyatlari, qulayliklari va tahdidlar strategik audit yuli bilan olib boriladi",2);
        addQ(s, "Marketing tadqiqotlari necha bosqichdan iborat?", "6","4","7","5","5",1);
    }

    private void addQ(Subject s, String q, String a, String b, String c, String d, String ans, int diff) {
        Question question = new Question();
        question.setSubject(s);
        question.setQuestionText(q);
        question.setOptionA(a);
        question.setOptionB(b);
        question.setOptionC(c);
        question.setOptionD(d);
        question.setCorrectAnswer(ans);
        question.setDifficulty(diff);
        questionRepository.save(question);
    }
}
