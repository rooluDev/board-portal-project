import {createRouter, createWebHistory} from "vue-router";
import Main from '@/pages/Main.vue';
import Login from '@/pages/Login.vue';
import Join from '@/pages/Join.vue';
import NoticeList from '@/pages/notice/NoticeList.vue';
import NoticeView from '@/pages/notice/NoticeView.vue';
import FreeList from '@/pages/free/FreeList.vue';
import FreeView from '@/pages/free/FreeView.vue';
import FreeWrite from '@/pages/free/FreeWrite.vue';
import FreeModify from '@/pages/free/FreeModify.vue';
import GalleryList from '@/pages/gallery/GalleryList.vue';
import GalleryView from '@/pages/gallery/GalleryView.vue';
import GalleryWrite from '@/pages/gallery/GalleryWrite.vue';
import GalleryModify from '@/pages/gallery/GalleryModify.vue';
import InquiryList from '@/pages/inquiry/InquiryList.vue';
import InquiryView from '@/pages/inquiry/InquiryView.vue';
import InquiryWrite from '@/pages/inquiry/InquiryWrite.vue';
import InquiryModify from '@/pages/inquiry/InquiryModify.vue';


const router = createRouter({
    history: createWebHistory(),
    routes:[
        {
            path: '/',
            name: 'Main',
            component: Main,
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
        },
        {
            path: '/join',
            name: 'Join',
            component: Join,
        },
        {
            path: '/boards/notice',
            name: 'Notice-List',
            component: NoticeList ,
        },
        {
            path: '/boards/notice/:id',
            name: 'Notice-View',
            component: NoticeView,
        },
        {
            path: '/boards/free',
            name: 'Free-List',
            component: FreeList,
        },
        {
            path: '/boards/free/:id',
            name: 'Free-View',
            component: FreeView,
        },
        {
            path: '/boards/free/write',
            name: 'Free-Write',
            component: FreeWrite,
        },
        {
            path: '/boards/free/modify/:id',
            name: 'Free-Modify',
            component: FreeModify,
        },
        {
            path: '/boards/gallery',
            name: 'Gallery-List',
            component: GalleryList,
        },
        {
            path: '/boards/gallery/:id',
            name: 'Gallery-View',
            component: GalleryView,
        },
        {
            path: '/boards/gallery/write',
            name: 'Gallery-Write',
            component: GalleryWrite,
        },
        {
            path: '/boards/gallery/modify/:id',
            name: 'Gallery-Modify',
            component: GalleryModify,
        },
        {
            path: '/boards/inquiry',
            name: 'Inquiry-List',
            component: InquiryList,
        },
        {
            path: '/boards/inquiry/:id',
            name: 'Inquiry-View',
            component: InquiryView,
        },
        {
            path: '/boards/inquiry/write',
            name: 'Inquiry-Write',
            component: InquiryWrite,
        },
        {
            path: '/boards/inquiry/modify/:id',
            name: 'Inquiry-Modify',
            component: InquiryModify,
        },
    ]
})

export default router;