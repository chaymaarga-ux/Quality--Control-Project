import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ProjectListComponent} from './components/project-list/project-list.component';
import {ProjectTasksComponent} from './components/project-tasks/project-tasks.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {SubtasksComponent} from './components/subtasks/subtasks.component';
import {FilterManagmentComponent} from './components/filter-managment/filter-managment.component';
import {LoginComponent} from './components/login/login.component';
import {authGuard} from './guards/auth.guard';
import {SignupComponent} from './components/signup/signup.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: '', redirectTo:"/login", pathMatch: "full" },
  { path: 'projects', component: ProjectListComponent , canActivate: [authGuard] },
  { path: 'project-tasks/:projectId', component: ProjectTasksComponent , canActivate: [authGuard] },
  { path: 'subtasks/:taskId' , component: SubtasksComponent , canActivate: [authGuard]},
  { path: 'dashboard', component: DashboardComponent , canActivate: [authGuard]},
  { path: 'filter', component: FilterManagmentComponent , canActivate: [authGuard]},


  // { path: '', redirectTo:"/dashboard", pathMatch: "full" },

  /*  { path: '', redirectTo: '/projects', pathMatch: 'full' }*/
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
