package teamProject_Server.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamProject_Server.Domain.Color;
import teamProject_Server.Domain.Style;
import teamProject_Server.Domain.User;
import teamProject_Server.Repository.ColorRepository;
import teamProject_Server.Repository.StyleRepository;
import teamProject_Server.Repository.UserRepository;

import java.util.*;

@Service
public class UserPreferenceService {

    private final UserRepository userRepository;
    private final ColorRepository colorRepository;
    private final StyleRepository styleRepository;

    @Autowired
    public UserPreferenceService(UserRepository userRepository, ColorRepository colorRepository, StyleRepository styleRepository) {
        this.userRepository = userRepository;
        this.colorRepository = colorRepository;
        this.styleRepository = styleRepository;
    }

    // 유저 선호도 업데이트 -> 결과값 반환
    // 결과값을 반환 할 입시리파지토리(DTO) 구성 필요
    public Map<String, Double> updateUserPreferences(String userEmail, String userColor, String userStyle, Long numC, Long numS) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (userColor != null) {
            // 유저 선호색 가중치 증가
            Color color = colorRepository.findByUser(user);
            updateColorPreferenceCount(color, userColor, numC);
        }

        if (userStyle != null) {
            // 유저 선호스타일 가중치 증가
            Style style = styleRepository.findByUser(user);
            updateStylePreferenceCount(style, userStyle, numS);
        }

        // 사용자의 선호색을 기반으로 3순위 정렬
        Color color = colorRepository.findByUser(user);
        Map<String, Double> topThreeColors = getTopThreeColors(color);
        
        // 사용자의 선호스타일을 기반으로 3순위 정렬
        Style style = styleRepository.findByUser(user);
        Map<String, Double> topThreeStyles = getTopThreeStyles(style);

        Map<String, Double> mergedMap = new HashMap<>();
        mergedMap = mergeMaps(topThreeStyles,topThreeColors);

        return mergedMap;
    }

    // 유저 선호색 학습
    private void updateColorPreferenceCount(Color color, String userColor, Long num) {
        switch (userColor.toLowerCase()) {
            case "red":
                color.setCol_red((int) (color.getCol_red() + num));
                break;
            case "orange":
                color.setCol_orange((int) (color.getCol_orange() + num));
                break;
            case "yellow":
                color.setCol_yellow((int) (color.getCol_yellow() + num));
                break;
            case "green":
                color.setCol_green((int) (color.getCol_green() + num));
                break;
            case "blue":
                color.setCol_blue((int) (color.getCol_blue() + num));
                break;
            case "purple":
                color.setCol_purple((int) (color.getCol_purple() + num));
                break;
            case "brown":
                color.setCol_brown((int) (color.getCol_brown() + num));
                break;
            case "gray":
                color.setCol_gray((int) (color.getCol_gray() + num));
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 색상입니다.");
        }
        colorRepository.save(color);
    }

    // 유저 선호 스타일 학습
    private void updateStylePreferenceCount(Style style, String userStyle, Long num) {
        switch (userStyle.toLowerCase()) {
            case "street":
                style.setSty_street((int) (style.getSty_street() + num));
                break;
            case "modern":
                style.setSty_modern((int) (style.getSty_modern() + num));
                break;
            case "minimal":
                style.setSty_minimal((int) (style.getSty_minimal() + num));
                break;
            case "feminine":
                style.setSty_feminine((int) (style.getSty_feminine() + num));
                break;
            case "simple basic":
                style.setSty_simpleBasic((int) (style.getSty_simpleBasic() + num));
                break;
            case "american casual":
                style.setSty_americanCasual((int) (style.getSty_americanCasual() + num));
                break;
            case "business casual":
                style.setSty_businessCasual((int) (style.getSty_businessCasual() + num));
                break;
            case "casual":
                style.setSty_casual((int) (style.getSty_casual() + num));
                break;
            case "retro":
                style.setSty_retro((int) (style.getSty_retro() + num));
                break;
            case "sports":
                style.setSty_sports((int) (style.getSty_sports() + num));
                break;
            case "classic":
                style.setSty_classic((int) (style.getSty_classic() + num));
                break;
            case "elegant":
                style.setSty_elegant((int) (style.getSty_elegant() + num));
                break;
            case "girlish":
                style.setSty_girlish((int) (style.getSty_girlish() + num));
                break;
            case "tomboy":
                style.setSty_tomboy((int) (style.getSty_tomboy() + num));
                break;
            case "vintage":
                style.setSty_vintage((int) (style.getSty_vintage() + num));
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 스타일입니다.");
        }
        styleRepository.save(style);
    }

    // 사용자 선호색 분류
    public Map<String, Double> getTopThreeColors(Color userColor) {

        // 주어진 사용자의 색 데이터 가져오기
        Map<String, Integer> colorData = getColorMap(userColor);

        // 색 데이터를 총합에 따라 정렬
        List<Map.Entry<String, Integer>> sortedColors = colorData.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        // 상위 3개 색의 총합 계산
        int topThreeSum = sortedColors.stream()
                .limit(3)
                .mapToInt(Map.Entry::getValue)
                .sum();

        // 상위 3개 색의 백분율 계산
        Map<String, Double> topThreeColorsWithPercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : sortedColors.subList(0, Math.min(3, sortedColors.size()))) {
            double percentage = (double) entry.getValue() / topThreeSum * 100.0;
            topThreeColorsWithPercentage.put(entry.getKey(), percentage);
        }

        return topThreeColorsWithPercentage;
    }

    // 사용자 선호색 분류
    public Map<String, Double> getTopThreeStyles(Style userStyle) {

        // 주어진 사용자의 색 데이터 가져오기
        Map<String, Integer> styleData = getStyleMap(userStyle);

        // 스타일 데이터를 총합에 따라 정렬
        List<Map.Entry<String, Integer>> sortedStyle = styleData.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        // 상위 3개 스타일 총합 계산
        int topThreeSum = sortedStyle.stream()
                .limit(3)
                .mapToInt(Map.Entry::getValue)
                .sum();

        // 상위 3개 스타일의 백분율 계산
        Map<String, Double> topThreeStyleWithPercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : sortedStyle.subList(0, Math.min(3, sortedStyle.size()))) {
            double percentage = (double) entry.getValue() / topThreeSum * 100.0;
            topThreeStyleWithPercentage.put(entry.getKey(), percentage);
        }

        return topThreeStyleWithPercentage;
    }

    // 스타잏맵과 선호색맵 병합
    private Map<String, Double> mergeMaps(Map<String, Double> map1, Map<String, Double> map2) {
        Map<String, Double> mergedMap = new HashMap<>();
        mergedMap.putAll(map1);
        mergedMap.putAll(map2);
        return mergedMap;
    }


    // --------------------------------------------------------------------------------------------
    // 사용자 선호도 가져오는 데이터 메서드
    private Map<String, Integer> getColorMap(Color userColor) {
        Map<String, Integer> colorData = new HashMap<>();
        colorData.put("red", userColor.getCol_red());
        colorData.put("orange", userColor.getCol_orange());
        colorData.put("yellow", userColor.getCol_yellow());
        colorData.put("green", userColor.getCol_green());
        colorData.put("blue", userColor.getCol_blue());
        colorData.put("purple", userColor.getCol_purple());
        colorData.put("brown", userColor.getCol_brown());
        colorData.put("gray", userColor.getCol_gray());
        return colorData;
    }

    private Map<String, Integer> getStyleMap(Style userStyle) {
        Map<String, Integer> styleData = new HashMap<>();
        // 주어진 사용자의 스타일 데이터 가져오기
        styleData.put("street", userStyle.getSty_street());
        styleData.put("modern", userStyle.getSty_modern());
        styleData.put("minimal", userStyle.getSty_minimal());
        styleData.put("feminine", userStyle.getSty_feminine());
        styleData.put("simpleBasic", userStyle.getSty_simpleBasic());
        styleData.put("americanCasual", userStyle.getSty_americanCasual());
        styleData.put("businessCasual", userStyle.getSty_businessCasual());
        styleData.put("casual", userStyle.getSty_casual());
        styleData.put("retro", userStyle.getSty_retro());
        styleData.put("sports", userStyle.getSty_sports());
        styleData.put("classic", userStyle.getSty_classic());
        styleData.put("elegant", userStyle.getSty_elegant());
        styleData.put("girlish", userStyle.getSty_girlish());
        styleData.put("tomboy", userStyle.getSty_tomboy());
        styleData.put("vintage", userStyle.getSty_vintage());

        return styleData;
    }

}


