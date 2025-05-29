import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Mainpage from './pages/MainPage/Mainpage';   // 메인 페이지
import Loginpage from './pages/LoginPage/Loginpage';  // 로그인 페이지
import SignupTerms from './pages/SignupPage/SignupTerms';   // 약관 동의 페이지
import SignupForm from './pages/SignupPage/SignupForm';     // 회원정보 입력 페이지
import GraduationCheckPage from './pages/GraduationCheckPage/GraduationCheckPage';    // 졸업자가진단 페이지
import FindPasswordEmail from './pages/FindPasswordPage/FindPasswordEmail';   // 비밀번호찾기 이메일 입력 페이지
import FindPasswordCode from './pages/FindPasswordPage/FindPasswordCode';   // 비밀번호찾기 이메일 코드 페이지
import ResetPassword from './pages/FindPasswordPage/ResetPassword';   // 비밀번호찾기 새비밀번호 생성 페이지
import MyPage from './pages/MyPage/MyPage';  // 나의 정보 페이지
import CommunityBoard from './pages/CommunityPage/CommunityPage';   // 커뮤니티 게시판 페이지
import BoardDetailPage from './pages/CommunityPage/BoardDetailPage';   // 게시글 페이지

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Mainpage />} />
        <Route path="/login" element={<Loginpage />} />
        <Route path="/signup/terms" element={<SignupTerms />} />
        <Route path="/signup/form" element={<SignupForm />} />
        <Route path="/grad-check" element={<GraduationCheckPage />} />
        <Route path="/find-password/email" element={<FindPasswordEmail />} />
        <Route path="/find-password/code" element={<FindPasswordCode />} />
        <Route path="/find-password/reset" element={<ResetPassword />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/communityboard" element={<CommunityBoard />} />
        <Route path="/board/:id" element={<BoardDetailPage />} />
      </Routes>
    </Router>
  );
}

export default App;


