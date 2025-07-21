package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Role;
import com.example.demo.entity.Staff;
import com.example.demo.entity.StaffRole;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StaffRepository;
import com.example.demo.repository.StaffRoleRepository;

@Service
public class PermissionAdminService {

    @Autowired
    private StaffRoleRepository staffRoleRepo;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 为员工更新角色（替换现有角色，若已有角色则先删除）
     */
    @Transactional
    public void updateRoleForStaff(Integer staffId, Integer roleId) {
        // 1. 验证员工和角色存在
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("员工不存在：" + staffId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在：" + roleId));

        // 2. 强制删除该员工的所有旧角色关联（无论是否存在）
        staffRoleRepo.deleteByStaffId(staffId);

        // 3. 插入新的角色关联
        StaffRole relation = new StaffRole();
        relation.setStaff(staff);
        relation.setRole(role);
        staffRoleRepo.save(relation);
    }

    /**
     * 添加新用户（仅保存基本信息，角色通过updateRoleForStaff分配）
     */
    public Staff addUser(Staff staff) {
        // 检查员工编码是否已存在
        if (staffRepository.existsByStaffcode(staff.getStaffcode())) {
            throw new IllegalArgumentException("员工编码已存在：" + staff.getStaffcode());
        }
        return staffRepository.save(staff);
    }
    /**
     * 添加新用户并分配角色（原子操作）
     */
    @Transactional // 确保事务一致性
    public Staff addUserAndAssignRole(Staff staff, Integer roleId) {
        // 1. 保存用户
        Staff savedStaff = this.addUser(staff);
        // 2. 分配角色
        this.updateRoleForStaff(savedStaff.getStaffId(), roleId);
        return savedStaff;
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Integer staffId) {
        // 先删除用户的角色关联
        staffRoleRepo.deleteByStaffId(staffId);
        // 再删除用户本身
        staffRepository.deleteById(staffId);
    }


    /**
     * 查询用户当前角色
     */
    public Role getUserRole(Integer staffId) {
        return staffRoleRepo.findRoleByStaffId(staffId)
                .orElseThrow(() -> new IllegalArgumentException("员工未分配角色：" + staffId));
    }
}