
(ns app.price-quote-templates.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :business
                                                            :icon       :description
                                                            :icon-color "#5da7d9"
                                                            :label      :price-quote-templates
                                                            :on-click   [:x.router/go-to! "/@app-home/price-quote-templates"]
                                                            :horizontal-weight 0}]
                              [:home.sidebar/add-menu-item! {:group      :business
                                                             :icon       :description
                                                             :icon-color "#5da7d9"
                                                             :label      :price-quote-templates
                                                             :on-click   [:x.router/go-to! "/@app-home/price-quote-templates"]
                                                             :vertical-weight 0}]]}})
