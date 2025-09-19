import { Component } from '@angular/core';
import {Filter} from '../../classes/Filter';
import {FilterService} from '../../services/filter.service';
import {MatDialog} from '@angular/material/dialog';
import {AddFilterFormComponent} from '../add-filter-form/add-filter-form.component';

@Component({
  selector: 'app-filter-managment',
  standalone: false,
  templateUrl: './filter-managment.component.html',
  styleUrl: './filter-managment.component.css'
})
export class FilterManagmentComponent {
  filters: Filter[] = [];

  constructor(private filterService: FilterService , private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadFilters();
  }

  loadFilters(): void {
    this.filterService.getAllFilters().subscribe(data => {
      this.filters = data;
    });
  }
  openDialog(): void {
    const dialogRef = this.dialog.open(AddFilterFormComponent, {
      width: '500px',
      disableClose: true,
      autoFocus: false,
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.filters.push(result); // ou appelle this.loadFilters();
      }
    });
  }


  editFilter(filter: Filter): void {
    filter.selected = true;
  }

/*  saveFilter(filter: Filter): void {
    if (filter.filterId) {
      this.filterService.updateFilter(filter.filterId, filter).subscribe(() => {
        filter.selected = false;
      });
    } else {
      this.filterService.addFilter(filter).subscribe(saved => {
        Object.assign(filter, saved);
        filter.selected = false;
      });
    }
  }*/

  updateFilterForm(id: number, filter: Filter) {
    filter.selected = !filter.selected;
  }

  updateFilter(id: number, filter: Filter) {
    console.log('filterId =', filter.filterId);
    console.log('Id =', id);
    this.filterService.updateFilter(id, filter).subscribe(
      () => {
        this.loadFilters();
        //window.location.reload();
      },
      (error) => {
        console.error('Error editing filter:', error);
      }
    );
  }

  cancelEdit(filter: Filter): void {
    filter.selected = false;
    this.loadFilters();
  }

  deleteFilter(id: number): void {
    if (confirm('Confirmer la suppression de ce filtre ?')) {
      this.filterService.deleteFilter(id).subscribe(() => {
        this.filters = this.filters.filter(f => f.filterId !== id);
      });
    }
  }

}
