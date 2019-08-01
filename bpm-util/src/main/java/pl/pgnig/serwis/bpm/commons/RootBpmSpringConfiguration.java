/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author jerzy.malyszko
 */
@Configuration
@ComponentScan(basePackages = "pl.pgnig.serwis.bpm")
@EntityScan("pl.pgnig.serwis.bpm")
@EnableJpaRepositories("pl.pgnig.serwis.bpm")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class RootBpmSpringConfiguration {

}
