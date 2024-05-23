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
import Error from "@/pages/Error.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'Main',
            component: Main,
            meta: {
                title: '메인 페이지'
            }
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
            meta: {
                title: '로그인'
            }
        },
        {
            path: '/join',
            name: 'Join',
            component: Join,
            meta: {
                title: '회원가입'
            }
        },
        {
            path: '/boards/notice',
            name: 'Notice-List',
            component: NoticeList,
            meta: {
                title: '공지사항 게시판'
            }
        },
        {
            path: '/boards/notice/:id',
            name: 'Notice-View',
            component: NoticeView,
            meta: {
                title: '공지사항 게시판'
            }
        },
        {
            path: '/boards/free',
            name: 'Free-List',
            component: FreeList,
            meta: {
                title: '자유 게시판'
            }
        },
        {
            path: '/boards/free/:id',
            name: 'Free-View',
            component: FreeView,
            meta: {
                title: '자유 게시판'
            }
        },
        {
            path: '/boards/free/write',
            name: 'Free-Write',
            component: FreeWrite,
            meta: {
                title: '자유 게시판'
            }
        },
        {
            path: '/boards/free/modify/:id',
            name: 'Free-Modify',
            component: FreeModify,
            meta: {
                title: '자유 게시판'
            }
        },
        {
            path: '/boards/gallery',
            name: 'Gallery-List',
            component: GalleryList,
            meta: {
                title: '갤러리 게시판'
            }
        },
        {
            path: '/boards/gallery/:id',
            name: 'Gallery-View',
            component: GalleryView,
            meta: {
                title: '갤러리 게시판'
            }
        },
        {
            path: '/boards/gallery/write',
            name: 'Gallery-Write',
            component: GalleryWrite,
            meta: {
                title: '갤러리 게시판'
            }
        },
        {
            path: '/boards/gallery/modify/:id',
            name: 'Gallery-Modify',
            component: GalleryModify,
            meta: {
                title: '갤러리 게시판'
            }
        },
        {
            path: '/boards/inquiry',
            name: 'Inquiry-List',
            component: InquiryList,
            meta: {
                title: '문의 게시판'
            }
        },
        {
            path: '/boards/inquiry/:id',
            name: 'Inquiry-View',
            component: InquiryView,
            meta: {
                title: '문의 게시판'
            }
        },
        {
            path: '/boards/inquiry/write',
            name: 'Inquiry-Write',
            component: InquiryWrite,
            meta: {
                title: '문의 게시판'
            }
        },
        {
            path: '/boards/inquiry/modify/:id',
            name: 'Inquiry-Modify',
            component: InquiryModify,
            meta: {
                title: '문의 게시판'
            }
        },
        {
            path: '/error',
            name: 'Error',
            component: Error,
            meta: {
                title: '에러'
            }
        }
    ]
})

router.beforeEach((to, from, next) => {
    document.title = to.meta.title || 'App';
    next();
});

export default router;