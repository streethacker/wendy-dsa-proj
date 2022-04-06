package multithread.weather.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RealtimeWeatherBo {

  /** 城市ID */
  @JsonProperty("cityid")
  private String cityId;

  /** 城市名称 */
  @JsonProperty("city")
  private String cityName;

  /** 更新时间 */
  @JsonProperty("update_time")
  private String updateTime;

  /** 天气情况 */
  @JsonProperty("wea")
  private String weather;

  /** 实时温度 */
  @JsonProperty("tem")
  private String temperature;

  /** 白天温度（高温） */
  @JsonProperty("tem_day")
  private String dayTemperature;

  /** 夜间温度（低温） */
  @JsonProperty("tem_night")
  private String nightTemperature;

  /** 风向 */
  @JsonProperty("win")
  private String wind;

  /** 风力等级 */
  @JsonProperty("win_speed")
  private String windSpeed;

  /** 风速 */
  @JsonProperty("win_meter")
  private String windMeter;

  /** 空气质量 */
  @JsonProperty("air")
  private String air;

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getWeather() {
    return weather;
  }

  public void setWeather(String weather) {
    this.weather = weather;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getDayTemperature() {
    return dayTemperature;
  }

  public void setDayTemperature(String dayTemperature) {
    this.dayTemperature = dayTemperature;
  }

  public String getNightTemperature() {
    return nightTemperature;
  }

  public void setNightTemperature(String nightTemperature) {
    this.nightTemperature = nightTemperature;
  }

  public String getWind() {
    return wind;
  }

  public void setWind(String wind) {
    this.wind = wind;
  }

  public String getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(String windSpeed) {
    this.windSpeed = windSpeed;
  }

  public String getWindMeter() {
    return windMeter;
  }

  public void setWindMeter(String windMeter) {
    this.windMeter = windMeter;
  }

  public String getAir() {
    return air;
  }

  public void setAir(String air) {
    this.air = air;
  }

  @Override
  public String toString() {
    return "RealtimeWeatherBo{"
        + "cityId='"
        + cityId
        + '\''
        + ", cityName='"
        + cityName
        + '\''
        + ", updateTime='"
        + updateTime
        + '\''
        + ", weather='"
        + weather
        + '\''
        + ", temperature='"
        + temperature
        + '\''
        + ", dayTemperature='"
        + dayTemperature
        + '\''
        + ", nightTemperature='"
        + nightTemperature
        + '\''
        + ", wind='"
        + wind
        + '\''
        + ", windSpeed='"
        + windSpeed
        + '\''
        + ", windMeter='"
        + windMeter
        + '\''
        + ", air='"
        + air
        + '\''
        + '}';
  }
}
