import {Component, OnInit} from '@angular/core';
import {ProjectService} from '../../services/project.service';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {JiraService} from '../../services/jira.service';
import {Project} from '../../classes/Project';
import {Task} from '../../classes/Task';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{
  allResults: {
    filterCode: string;
    project: Project;
    issue: Task;
  }[] = [];



  allProjects: Project[] = [];

  constructor(private jiraService: JiraService, private projectService: ProjectService) {}

  groupedResults: { [projectKey: string]: { [filterCode: string]: any[] } } = {};
  projectKeys: string[] = [];
  filterCodes: string[] = [];

 /* ngOnInit(): void {
    this.jiraService.getGroupedFilteredResults().subscribe({
      next: data => {
        this.groupedResults = data;
        this.projectKeys = Object.keys(data);
        console.log('✅ Données reçues :', data);

        // Extraire tous les codes filtres uniques
        const allFilterCodes = new Set<string>();
        for (const filtersByProject of Object.values(data)) {
          Object.keys(filtersByProject).forEach(code => allFilterCodes.add(code));
        }
        this.filterCodes = Array.from(allFilterCodes).sort();

        // Transformer en tableau pour l'affichage ligne par ligne
        this.allResults = [];
        for (const [projectKey, filters] of Object.entries(data)) {
          for (const [filterCode, issues] of Object.entries(filters)) {
            for (const issue of issues) {
              const mappedTask: Task = {
                taskKey: issue.key,
                summary: issue.fields?.summary,
                status: issue.fields?.status?.name, // ou mapper vers ton TaskStatusEnum si souhaité
                createdDate: issue.fields?.created,
                assigneeDisplayName: issue.fields?.assignee?.displayName,
                assigneeEmail: issue.fields?.assignee?.emailAddress,
                creatorDisplayName: issue.fields?.creator?.displayName,
                creatorEmail: issue.fields?.creator?.emailAddress,
                reporterDisplayName: issue.fields?.reporter?.displayName,
                reporterEmail: issue.fields?.reporter?.emailAddress,
                // ajoute d'autres mappings si nécessaire
              };

              this.allResults.push({
                filterCode,
                project: { projectKey: projectKey, name: projectKey }, // adapter si nécessaire
                issue: mappedTask
              });
            }
          }
        }
        console.log('✅ Données transformées pour affichage:', this.allResults);
      },
      error: err => {
        console.error('❌ Erreur de chargement des données filtrées groupées', err);
      }
    });
    console.log(this.groupedResults);
    console.log(this.allResults);

  }*/
  ngOnInit(): void {
    const cachedData = localStorage.getItem('groupedResults');

    if (cachedData) {
      console.log('✅ Données chargées depuis le cache localStorage');
      const data = JSON.parse(cachedData);
      this.handleData(data);
    } else {
      this.jiraService.getGroupedFilteredResults().subscribe({
        next: data => {
          localStorage.setItem('groupedResults', JSON.stringify(data));
          this.handleData(data);
         // window.location.reload();

        },
        error: err => {
          console.error('❌ Erreur de chargement des données filtrées groupées', err);
        }
      });
    }
  }

  handleData(data: any): void {
    this.groupedResults = data;
    this.projectKeys = Object.keys(data);
    console.log('✅ Données reçues :', data);

    const allFilterCodes: Set<string> = new Set<string>();
    for (const filtersByProject of Object.values(data) as any[]) {
      Object.keys(filtersByProject).forEach((code: string) => allFilterCodes.add(code));
    }
    this.filterCodes = Array.from(allFilterCodes).sort();

    this.allResults = [];
    for (const [projectKey, filters] of Object.entries(data)) {
      for (const [filterCode, issues] of Object.entries(filters as { [key: string]: any })) {
        for (const issue of issues as any[]) {
          const mappedTask: Task = {
            taskKey: issue.key,
            summary: issue.fields?.summary,
            status: issue.fields?.status?.name,
            createdDate: issue.fields?.created,
            assigneeDisplayName: issue.fields?.assignee?.displayName,
            assigneeEmail: issue.fields?.assignee?.emailAddress,
            creatorDisplayName: issue.fields?.creator?.displayName,
            creatorEmail: issue.fields?.creator?.emailAddress,
            reporterDisplayName: issue.fields?.reporter?.displayName,
            reporterEmail: issue.fields?.reporter?.emailAddress,
          };

          this.allResults.push({
            filterCode,
            project: { projectKey: projectKey, name: projectKey },
            issue: mappedTask
          });
        }
      }
    }
    console.log('✅ Données transformées pour affichage:', this.allResults);
  }




  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }


/*  ngOnInit(): void {
    this.projectService.getAllProjects().subscribe({
      next: (projects) => {
        this.allProjects = projects;

        const filterCodes = [
          '1', '2', '3', '4', '5', '6', '7', '8', '9',
          '10.1', '10.2', '10.3', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20'
        ];

        filterCodes.forEach(code => {
          this.jiraService.getFilteredIssuesByCode(code).subscribe({
            next: data => {
              for (const [projectKey, tasks] of Object.entries(data)) {
                const project = this.allProjects.find(p => p.projectKey === projectKey);
                if (!project) {
                  console.warn(`⚠️ Projet non trouvé pour la clé : ${projectKey}`);
                  continue;
                }

                for (const task of tasks) {
                  this.allResults.push({
                    filterCode: code,
                    project: project,
                    issue: task
                  });
                }
              }
            },
            error: err => {
              console.error(`❌ Erreur pour le filtre ${code}`, err);
            }
          });
        });
      },
      error: err => {
        console.error('❌ Erreur lors du chargement des projets', err);
      }
    });
  }*/



  leftTableData = [
    { key: 'HPCQUALITY-56297', freeTextField4: 'RPA-ORANGE-BELGIUM', aggrupation: 'C - Conforme', freeComboField1: 'Plan de Mejora', created: '25/04/2025' },
    { key: 'HPCQUALITY-56296', freeTextField4: 'RPA-ORANGE-BELGIUM', aggrupation: 'C - Conforme', freeComboField1: 'Plan de Mejora', created: '25/04/2025' },
    // ... add more data
  ];

  rightTableData = [
    { key: 'HPCQUALITY-55278', freeTextField4: 'IT - Intesa Nautilus Project', aggrupation: 'NC - No Conformidad', freeComboField1: 'Gestión de Riesgos', created: '14/04/2025' },
    { key: 'HPCQUALITY-54355', freeTextField4: 'IT - Intesa Nautilus Project', aggrupation: 'NCI - No Conformidad Informativa', freeComboField1: 'Ciclo de vida Standard', created: '08/04/2025' },
    // ... add more data
  ];

  getStatusClass(status: string): string {
    switch (status) {
      case 'C - Conforme':
        return 'status-conforme';
      case 'NA - No Aplica':
        return 'status-aplica';
      case 'NC - No Conformidad':
        return 'status-no-conformidad';
      case 'NCI - No Conformidad Informativa':
        return 'status-no-conformidad-info';
      default:
        return '';
    }
  }
}
