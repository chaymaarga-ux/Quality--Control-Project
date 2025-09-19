import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProjectListComponent } from './components/project-list/project-list.component';
import {HttpClientModule} from '@angular/common/http';
import {TableModule} from 'primeng/table';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {IconFieldModule} from 'primeng/iconfield';
import {InputIconModule} from 'primeng/inputicon';
import { ProjectTasksComponent } from './components/project-tasks/project-tasks.component';
import {Paginator, PaginatorModule} from 'primeng/paginator';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {MessagesModule} from 'primeng/messages';
import {MessageService} from 'primeng/api';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastModule} from 'primeng/toast';
import { SubtasksComponent } from './components/subtasks/subtasks.component';
import { FilterManagmentComponent } from './components/filter-managment/filter-managment.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import { AddFilterFormComponent } from './components/add-filter-form/add-filter-form.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';

@NgModule({
  declarations: [
    AppComponent,
    ProjectListComponent,
    ProjectTasksComponent,
    DashboardComponent,
    SubtasksComponent,
    FilterManagmentComponent,
    AddFilterFormComponent,
    LoginComponent,
    SignupComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
  IconFieldModule ,
  InputIconModule,
    PaginatorModule,
    Paginator,
    MessagesModule,
    BrowserAnimationsModule,
    ToastModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    ReactiveFormsModule,
  ],

  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
