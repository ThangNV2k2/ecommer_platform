"use client";
import { RootState } from '@/redux/store';
import { redirect } from 'next/navigation';
import { useSelector } from 'react-redux';

export default function Dashboard() {

  const userInfo = useSelector((state: RootState) => state.user.user);
  if (!userInfo) {
    return redirect('/');
  } else {
    redirect('/dashboard/overview');
  }
}
