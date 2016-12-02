package com.owngame.web;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.GroupName;
import com.owngame.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-7.
 */

@Controller
@RequestMapping("Smserver/contacts")
public class ContactController {
    @Autowired
    ContactService contactService;

    /**
     * 根据组名返回该组的联系人信息
     *
     * @param groupname
     * @return
     */
    @RequestMapping(value = "/{groupname}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getContactByGroup(@PathVariable("groupname") String groupname) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", contactService.queryByGroup(groupname));
        return map;
    }

    /**
     * 返回所有的分组信息
     *
     * @return
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGroups() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groups", contactService.getGroups());
        return map;
    }

    /**
     * 更新某个联系人的信息
     *
     * @param contactDisplay
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateContact(@RequestBody ContactDisplay contactDisplay) {
        if (contactDisplay.getBase_id() > 0) {//是更新
            contactService.update(contactDisplay);
        } else {
            System.out.println("insert contactDisplay!");
            contactDisplay.setBase_id(0);
            contactService.insert(contactDisplay);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("contacts", contactService.queryByGroup(contactDisplay.getGroupname()));
        return map;
    }

    /**
     * 删除联系人
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteContact(@RequestBody Map<String, Long> p) {
        // 先查询这个id属于那个组
        String groupname = contactService.queryById(p.get("id")).getGroupname();
        // 删除操作
        contactService.delete(p.get("id"));
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("contacts", contactService.queryByGroup(groupname));
        return map;
    }


    /**
     * 通过姓名查询联系人（模糊查询）
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/searchbyname", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchContactsByName(@RequestBody Map<String, String> p) {
        String name = p.get("name");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", contactService.queryLikeName("%" + name + "%"));
        return map;
    }

    /**
     * 通过ids查询联系人信息
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/searchbyids", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchContactsByIds(@RequestBody Map<String, String> p) {
        String ids = p.get("ids");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", contactService.getContactByIds(ids));
        return map;
    }


    /**
     * 对分组信息的增删改查
     *
     * @param p
     * @param action
     * @return
     */
    @RequestMapping(value = "/group/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleGroup(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        // 先判断操作
        if (action.equals("update")) {
            String ori = p.get("originalGroupName");
            String newName = p.get("groupname");
            contactService.updateGroup(new GroupName(0, ori, newName));
        } else if (action.equals("delete")) {
            contactService.deleteGroup(p.get("originalGroupName"));
        } else if (action.equals("insert")) {
            return contactService.insertGroup(p);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", "success");
        return map;
    }

}
