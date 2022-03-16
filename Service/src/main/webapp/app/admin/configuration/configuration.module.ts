import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AzilenNotificationServiceSharedModule } from 'app/shared/shared.module';

import { ConfigurationComponent } from './configuration.component';

import { configurationRoute } from './configuration.route';

@NgModule({
  imports: [AzilenNotificationServiceSharedModule, RouterModule.forChild([configurationRoute])],
  declarations: [ConfigurationComponent],
})
export class ConfigurationModule {}
