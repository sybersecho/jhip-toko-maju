import { Component, OnInit, Injectable } from '@angular/core';
import { User, UserService } from 'app/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { PasswordChangeService } from './password-change.service';

@Component({
    selector: 'jhi-password-change',
    templateUrl: './password-change.component.html',
    styles: []
})
export class PasswordChangeComponent implements OnInit {
    account: any;
    newPassword: string;
    confirmPassword: string;
    doNotMatch: string;
    success: string;
    error: string;
    user: User;

    constructor(
        private userService: UserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private changeService: PasswordChangeService
    ) {}

    ngOnInit() {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmChange() {
        if (this.newPassword !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
            return;
        }
        this.doNotMatch = null;
        this.changeService.save({ login: this.user.login, password: this.newPassword }).subscribe(
            () => {
                this.success = 'OK';
                this.activeModal.dismiss(true);
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
            }
        );

        // this.success = 'OK';
    }
}
