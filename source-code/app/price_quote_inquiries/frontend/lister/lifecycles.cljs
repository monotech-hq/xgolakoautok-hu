
(ns app.price-quote-inquiries.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :business
                                                            :icon       :request_page
                                                            :icon-color "#538cd7"
                                                            :label      :price-quote-inquiries
                                                            :on-click   [:router/go-to! "/@app-home/price-quote-inquiries"]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group      :business
                                                             :icon       :request_page
                                                             :icon-color "#538cd7"
                                                             :label      :price-quote-inquiries
                                                             :on-click   [:router/go-to! "/@app-home/price-quote-inquiries"]
                                                             :vertical-weight 1}]]}})
