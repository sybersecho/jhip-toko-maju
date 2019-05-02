import { Component, OnInit, Injectable, OnDestroy } from '@angular/core';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-search-project-dialog',
    templateUrl: './search-project-dialog.component.html',
    styles: []
})
export class SearchProjectDialogComponent implements OnInit {
    searchKeyword: string;
    projects: IProject[];

    constructor(public activeModal: NgbActiveModal, protected eventManager: JhiEventManager, protected projectService: ProjectService) {
        this.projects = [];
    }

    ngOnInit() {}

    search() {
        this.loadProject();
    }

    clearSearch() {
        this.searchKeyword = '';
        this.projects = [];
    }

    onSelect(project: IProject) {
        this.eventManager.broadcast({
            name: 'onSearchSelectProjectEvent',
            data: project
        });
        this.activeModal.dismiss(true);
    }

    protected loadProject() {
        if (!this.searchKeyword) {
            this.projects = [];
            return;
        }

        this.projectService.queryByCriteria({ query: this.searchKeyword }).subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = [];
                this.projects = res.body;
            },
            error => {
                this.projects = [];
                console.error(error.message);
            }
        );
    }
}

@Injectable({
    providedIn: 'root'
})
export class SearchProjectDialogService implements OnDestroy {
    private isOpen = false;
    protected ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal) {}

    open() {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        this.ngbModalRef = this.modalService.open(SearchProjectDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return this.ngbModalRef;
    }

    ngOnDestroy(): void {
        this.ngbModalRef = null;
    }
}
