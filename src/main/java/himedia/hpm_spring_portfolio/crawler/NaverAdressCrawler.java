/**
 * 파일명: NaverAdressCrawler.java
 * 작성자: 문호정
 * 설명: 네이버 검색을 통해 산의 주소 정보를 크롤링하고, 이를 CSV 파일로 저장하는 자동화 프로그램
 *        - 산 이름을 기반으로 네이버 검색 수행
 *        - 주소 정보를 추출하여 CSV 파일로 저장
 * 작성일: 2025-04-15
 */

package himedia.hpm_spring_portfolio.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class NaverAdressCrawler {

    // 크롬 드라이버 경로
    private static final String DRIVER_PATH = "C:\\chromedriver-win64\\chromedriver.exe"; 

    public static void main(String[] args) {
        // 크롤링 대상 산 목록 (총 100개)
        String[] mountains = {
            "가리산", "가리왕산", "가야산", "가지산", "감악산", "강천산", "계룡산", "계방산", "공작산", "관악산",
            "구병산", "금산", "금수산", "금오산", "금정산", "깃대봉", "남산", "내연산", "내장산", "대둔산",
            "대암산", "대야산", "덕숭산(수덕산)", "덕유산(향적봉)", "덕항산", "도락산", "도봉산(자운봉)", "두륜산", "두타산", "마니산",
            "마이산", "명성산", "명지산", "모악산", "무등산", "무학산", "미륵산", "민주지산", "방장산", "방태산",
            "백덕산", "백암산", "백운산(광양)", "백운산(정선)", "백운산(포천)", "변산", "북한산", "비슬산", "삼악산", "서대산",
            "선운산", "설악산", "성인봉", "소백산", "소요산", "속리산", "신불산", "연화산", "오대산", "오봉산",
            "용문산", "용화산", "운문산", "운악산", "운장산", "월악산", "월출산", "유명산", "응봉산", "장안산",
            "재약산", "적상산", "점봉산", "조계산", "주왕산", "주흘산", "지리산", "지리산(통영)", "천관산", "천마산",
            "천성산", "천태산", "청량산", "추월산", "축령산", "치악산", "칠갑산", "태백산", "태화산", "팔공산",
            "팔봉산", "팔영산", "한라산", "화악산(중봉)", "화왕산", "황매산", "황석산", "황악산", "황장산", "희양산"
        };

        // CSV 파일 경로
        String csvFilePath = "mountain_addresses.csv";

        // CSV 작성 시작
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write("산 이름,주소");
            writer.newLine();

            for (String mountain : mountains) {
                String address = getMountainAddress(mountain);
                writer.write(mountain + "," + (address != null ? address : "주소를 찾을 수 없습니다."));
                writer.newLine();
            }

            System.out.println("✅ 데이터가 mountain_addresses.csv 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("❌ CSV 저장 실패: " + e.getMessage());
        }
    }


    //	네이버 검색을 통해 산 이름에 대한 주소 정보를 가져오는 메서드
    public static String getMountainAddress(String mountain) {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        String address = null;

        try {
            String url = "https://search.naver.com/search.naver?where=nexearch&query=" + mountain;
            driver.get(url);
            Thread.sleep(2000); // 페이지 로딩 대기

            // 주소를 포함하는 요소 탐색
            WebElement addressElement = driver.findElement(By.className("PkgBl"));
            address = addressElement.getText().replaceAll("상세정보", "").trim();
        } catch (Exception e) {
            System.err.println("❌ [" + mountain + "] 주소 크롤링 실패: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return address;
    }
}
