import {PriorityEnum} from '../enum/PriorityEnum';
import {TaskStatusEnum} from '../enum/TaskStatusEnum';

export interface Task {
  taskId?: number;
  url?: string;
  taskKey: string;
  type?: string;
  summary: string;
  priority?: PriorityEnum;
  description?: string;
  status?: TaskStatusEnum;
  issueTypeName?: string;
  issueTypeSubTask?: boolean;
  assignee?: string;
  reporterName?: string;
  reporterEmail?: string;
  reporterDisplayName?: string;
  assigneeEmail?: string;
  assigneeDisplayName?: string;
  creatorEmail?: string;
  creatorDisplayName?: string;
  createdDate?: Date | string;
  updatedDate?: Date | string;
  startDate?: Date | string;
  dueDate?: Date | string;
  resolvedDate?: Date | string;
  internalEstimate?: number;
  salesEstimate?: number;
  estimated?: number;
  ETC?: number; // Estimate to Complete //remaining estimate
  remaining?: number; //=ETC
  loggedHours?: number; //juste dans la tache mainn
aggregateLoggedHours?: number; //le temps total passé incluant les sous-tâches

  checklistInput?: string[];
  checklistOutput?: string[];
  doubts?: string[];
  projectId?: number;
}
