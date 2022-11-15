
(ns app.price-quote-inquiries.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name  :business
                                                            :icon        :request_quote
                                                            :icon-family :material-icons-outlined
                                                            :icon-color  "#5da7d9"
                                                            :label       :price-quote-inquiries
                                                            :on-click    [:x.router/go-to! "/@app-home/price-quote-inquiries"]
                                                            :horizontal-weight 2}]
                              [:home.sidebar/add-menu-item! {:group-name  :business
                                                             :icon        :request_quote
                                                             :icon-family :material-icons-outlined
                                                             :icon-color  "#5da7d9"
                                                             :label       :price-quote-inquiries
                                                             :on-click    [:x.router/go-to! "/@app-home/price-quote-inquiries"]
                                                             :vertical-weight 2}]]}})
