import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFilterFormComponent } from './add-filter-form.component';

describe('AddFilterFormComponent', () => {
  let component: AddFilterFormComponent;
  let fixture: ComponentFixture<AddFilterFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddFilterFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddFilterFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
