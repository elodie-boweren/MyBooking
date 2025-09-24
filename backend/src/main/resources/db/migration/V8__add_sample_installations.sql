-- Add sample installations for testing
INSERT INTO installation (name, description, installation_type, capacity, hourly_rate, currency, equipment) VALUES
('Spa Room 1', 'Luxury spa room with massage tables and relaxation area', 'SPA_ROOM', 10, 50.00, 'EUR', 'Massage tables, aromatherapy equipment'),
('Conference Room A', 'Large conference room with presentation equipment', 'CONFERENCE_ROOM', 20, 100.00, 'EUR', 'Projector, whiteboard, video conferencing'),
('Fitness Center', 'Modern gym with cardio and weight training equipment', 'GYM', 15, 30.00, 'EUR', 'Treadmills, weights, yoga mats'),
('Swimming Pool', 'Indoor heated pool for relaxation and exercise', 'POOL', 25, 40.00, 'EUR', 'Pool equipment, changing rooms'),
('Tennis Court', 'Outdoor tennis court with professional surface', 'TENNIS_COURT', 4, 60.00, 'EUR', 'Tennis rackets, balls, net'),
('Wedding Hall', 'Elegant hall for weddings and special events', 'WEDDING_ROOM', 50, 200.00, 'EUR', 'Sound system, lighting, decorations');
