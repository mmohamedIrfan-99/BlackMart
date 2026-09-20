package com.blackmart.blackmart.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalProducts;
    private long totalOrders;
    private long pendingProducts;
    private long pendingOrders;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(long totalUsers, long totalProducts,
                                  long totalOrders, long pendingProducts,
                                  long pendingOrders) {
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.pendingProducts = pendingProducts;
        this.pendingOrders = pendingOrders;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPendingProducts() {
        return pendingProducts;
    }

    public void setPendingProducts(long pendingProducts) {
        this.pendingProducts = pendingProducts;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
}
