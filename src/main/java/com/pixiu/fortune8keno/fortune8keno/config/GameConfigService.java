package com.pixiu.fortune8keno.fortune8keno.config;


import com.pixiu.fortune8keno.fortune8keno.config.rtp.*;
import com.pixiu.fortune8keno.fortune8keno.game.constants.RTP;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class GameConfigService {

  @Value("classpath:config.json")
  private Resource resource;

  private final String version;

  private final GitProperties gitProperties;
  private final Map<BigDecimal, GameConfiguration> gameConfigurationMap;

  private final GameMathConfigCommon gameMathConfigCommon;

  public GameConfigService(@Value("${version}") String version,
                           GitProperties gitProperties, GameMathConfigCommon gameMathConfigCommon) {
    this.version = version;
    this.gitProperties = gitProperties;
    this.gameMathConfigCommon = gameMathConfigCommon;
    String buildVersion = "build.version";
    gameConfigurationMap = Map.of(
            RTP.VARIANT_92, new GameConfiguration(this.gameMathConfigCommon, new GameMathConfigImp_92()).setEngineVersion(gitProperties.get(buildVersion)).setGameVersion(version),
            RTP.VARIANT_86, new GameConfiguration(this.gameMathConfigCommon, new GameMathConfigImp_86()).setEngineVersion(gitProperties.get(buildVersion)).setGameVersion(version)
    );
  }

  public GameConfigurationForClient getConfigForClient(BigDecimal rtpVariant) {
    return new GameConfigurationForClient(getConfig(rtpVariant));
  }

  public GameConfiguration getConfig(BigDecimal rtpVariant) {
    GameConfiguration defaultConfiguration  = gameConfigurationMap.get(RTP.VARIANT_DEFAULT);
    String rtpString = "defaultRtpString";
    if(rtpVariant != null) rtpString  = rtpVariant.toString();
    if(rtpVariant == null){
      // default to 92.0
      return defaultConfiguration;
    }
    GameConfiguration gameConfiguration = gameConfigurationMap.get(rtpVariant);

    if(gameConfiguration == null) {
      return defaultConfiguration.setRtpString(rtpString);
    }

    return gameConfiguration.setRtpString(rtpString);
  }

}
