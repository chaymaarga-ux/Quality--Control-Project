/*
export interface ProjectJiraDto {
  id: string;
  key: string;
  name: string;
  self: string;
  projectTypeKey: string;
  projectCategory?: {
    name: string;
    description: string;
  };
}

export interface ProjectCategory {
  name: string;
  description: string;
}
*/


export interface Project {
  projectId?: number;
  name: string;
  projectKey?: string;
  type?: string;
  url?: string;
  lead?: string;
  category?: string;
  description?: string;
  tasks?: any[];
}
