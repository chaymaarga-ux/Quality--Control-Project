import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilterManagmentComponent } from './filter-managment.component';

describe('FilterManagmentComponent', () => {
  let component: FilterManagmentComponent;
  let fixture: ComponentFixture<FilterManagmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FilterManagmentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FilterManagmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
