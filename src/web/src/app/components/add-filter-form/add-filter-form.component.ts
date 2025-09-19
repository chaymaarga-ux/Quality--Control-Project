import { Component } from '@angular/core';
import {Filter} from '../../classes/Filter';
import {FilterService} from '../../services/filter.service';
import { MatDialogRef } from '@angular/material/dialog';
@Component({
  selector: 'app-add-filter-form',
  standalone: false,
  templateUrl: './add-filter-form.component.html',
  styleUrl: './add-filter-form.component.css'
})
export class AddFilterFormComponent {

  newFilter: Filter = new Filter();

  isLoading = false;
  errorMessage: string | null = null;

  constructor(
    private filterService: FilterService,
    private dialogRef: MatDialogRef<AddFilterFormComponent>
  ) {}

  addFilter(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.filterService.addFilter(this.newFilter).subscribe({
      next: (savedFilter) => {
        this.isLoading = false;
        this.dialogRef.close(savedFilter); // ferme le dialogue et renvoie au parent
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Une erreur est survenue lors de l’enregistrement.';
        console.error('Erreur lors de l’ajout du filtre :', err);
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
