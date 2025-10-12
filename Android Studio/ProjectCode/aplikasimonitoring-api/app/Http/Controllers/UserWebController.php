<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class UserWebController extends Controller
{
    public function index() {
        $users = User::all();
        return view('admin.user.index', compact('users'));
    }

    public function store(Request $request) {
        $request->validate([
            'nama'=>'required',
            'email'=>'required|email|unique:users,email',
            'password'=>'required',
            'role'=>'required'
        ]);

        User::create([
            'nama'=>$request->nama,
            'email'=>$request->email,
            'password'=>Hash::make($request->password),
            'role'=>$request->role
        ]);

        return redirect()->back();
    }

    public function update(Request $request, User $user) {
        $request->validate([
            'nama'=>'required',
            'email'=>'required|email|unique:users,email,'.$user->id,
            'role'=>'required'
        ]);

        $user->update([
            'nama'=>$request->nama,
            'email'=>$request->email,
            'role'=>$request->role
        ]);

        if($request->password){
            $user->password = Hash::make($request->password);
            $user->save();
        }

        return redirect()->back();
    }

    public function destroy(User $user){
        $user->delete();
        return redirect()->back();
    }
}
